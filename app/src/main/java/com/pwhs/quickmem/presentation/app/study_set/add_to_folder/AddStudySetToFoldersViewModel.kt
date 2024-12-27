package com.pwhs.quickmem.presentation.app.study_set.add_to_folder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.study_set.AddStudySetToFoldersRequestModel
import com.pwhs.quickmem.domain.repository.FolderRepository
import com.pwhs.quickmem.domain.repository.StudySetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddStudySetToFoldersViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val appManager: AppManager,
    private val folderRepository: FolderRepository,
    private val studySetRepository: StudySetRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddStudySetToFoldersUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<AddStudySetToFoldersUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val studySetId: String = savedStateHandle.get<String>("studySetId") ?: ""
        _uiState.update { it.copy(studySetId = studySetId) }
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: return@launch
            val ownerId = appManager.userId.firstOrNull() ?: return@launch
            val userAvatar = appManager.userAvatarUrl.firstOrNull() ?: return@launch
            val username = appManager.username.firstOrNull() ?: return@launch
            _uiState.update {
                it.copy(
                    token = token,
                    userId = ownerId,
                    userAvatar = userAvatar,
                    username = username
                )
            }
            getFolders()
        }
    }

    fun onEvent(event: AddStudySetToFoldersUiAction) {
        when (event) {
            is AddStudySetToFoldersUiAction.AddStudySetToFolders -> {
                doneClick()
            }

            is AddStudySetToFoldersUiAction.ToggleStudySetImport -> {
                toggleFolderImport(event.folderId)
            }

            is AddStudySetToFoldersUiAction.RefreshFolders -> {
                getFolders()
            }
        }
    }

    private fun getFolders() {
        viewModelScope.launch {
            folderRepository.getFoldersByUserId(
                _uiState.value.token,
                _uiState.value.userId,
                null,
                _uiState.value.studySetId,
            ).collectLatest { resources ->
                when (resources) {
                    is Resources.Success -> {
                        _uiState.update { foldersUiState ->
                            foldersUiState.copy(
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
                            AddStudySetToFoldersUiEvent.Error(
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

    private fun doneClick() {
        viewModelScope.launch {
            val addStudySetToFoldersRequestModel = AddStudySetToFoldersRequestModel(
                studySetId = _uiState.value.studySetId,
                folderIds = _uiState.value.folderImportedIds,
            )
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            studySetRepository.addStudySetToFolders(
                token = token,
                addStudySetToFoldersRequestModel
            ).collectLatest { resources ->
                when (resources) {
                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.send(AddStudySetToFoldersUiEvent.StudySetAddedToFolders)
                    }

                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.send(
                            AddStudySetToFoldersUiEvent.Error(
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
