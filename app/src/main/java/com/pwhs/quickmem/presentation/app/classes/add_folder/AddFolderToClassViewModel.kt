package com.pwhs.quickmem.presentation.app.classes.add_folder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.folder.AddFolderToClassRequestModel
import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.domain.repository.FolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFolderToClassViewModel @Inject constructor(
    private val folderRepository: FolderRepository,
    private val appManager: AppManager,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _folders = MutableStateFlow<List<GetFolderResponseModel>>(emptyList())
    val folders: StateFlow<List<GetFolderResponseModel>> = _folders

    private val _uiState = MutableStateFlow(AddFolderToClassUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<AddFolderToClassUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val classId: String = savedStateHandle.get<String>("classId") ?: ""
        _uiState.update { it.copy(classId = classId) }
        viewModelScope.launch {
            val userAvatar = appManager.userAvatarUrl.firstOrNull() ?: return@launch
            val username = appManager.username.firstOrNull() ?: return@launch
            _uiState.update {
                it.copy(
                    userAvatar = userAvatar,
                    username = username
                )
            }
            getFolders()
        }
    }

    fun onEvent(event: AddFolderToClassUiAction) {
        when (event) {
            AddFolderToClassUiAction.AddFolderToClass -> {
                doneClick()
            }

            is AddFolderToClassUiAction.ToggleFolderImport -> {
                toggleFolderImport(event.folderId)
            }

            AddFolderToClassUiAction.RefreshFolders -> {
                getFolders()
            }
        }
    }

    private fun doneClick() {
        viewModelScope.launch {
            val addFolderToClassRequestModel = AddFolderToClassRequestModel(
                classId = _uiState.value.classId,
                folderIds = _uiState.value.folderImportedIds
            )
            folderRepository.addFolderToClass(addFolderToClassRequestModel = addFolderToClassRequestModel)
                .collectLatest { resources ->
                    when (resources) {
                        is Resources.Success -> {
                            _uiState.update {
                                it.copy(isLoading = false)
                            }
                            _uiEvent.send(AddFolderToClassUiEvent.StudySetAddedToClass)
                        }

                        is Resources.Error -> {
                            _uiState.update {
                                it.copy(isLoading = false)
                            }
                            _uiEvent.send(
                                AddFolderToClassUiEvent.ShowError(
                                    R.string.txt_error_occurred
                                )
                            )
                        }

                        is Resources.Loading -> {
                            _uiState.update {
                                it.copy(isLoading = true)
                            }
                        }
                    }
                }
        }
    }

    private fun getFolders() {
        viewModelScope.launch {
            folderRepository.getFoldersByUserId(
                _uiState.value.classId,
                null
            ).collectLatest { resources ->
                when (resources) {
                    is Resources.Success -> {
                        _uiState.update { uiState1 ->
                            uiState1.copy(
                                isLoading = false,
                                folders = resources.data ?: emptyList(),
                                folderImportedIds = resources.data?.filter { it.isImported == true }
                                    ?.map { it.id } ?: emptyList()
                            )
                        }
                    }

                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.send(
                            AddFolderToClassUiEvent.ShowError(
                                R.string.txt_error_occurred
                            )
                        )
                    }

                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                }
            }
        }
    }

    private fun toggleFolderImport(folderId: String) {
        val folderImportedIds = _uiState.value.folderImportedIds.toMutableList()
        if (folderImportedIds.contains(folderId)) {
            folderImportedIds.remove(folderId)
        } else {
            folderImportedIds.add(folderId)
        }
        _uiState.update {
            it.copy(folderImportedIds = folderImportedIds)
        }
    }
}