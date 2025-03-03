package com.pwhs.quickmem.presentation.app.folder.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.folder.UpdateFolderRequestModel
import com.pwhs.quickmem.domain.repository.FolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditFolderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val folderRepository: FolderRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditFolderUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<EditFolderUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val folderId: String = savedStateHandle.get<String>("folderId") ?: ""
        val folderTitle: String = savedStateHandle.get<String>("folderTitle") ?: ""
        val folderDescription: String = savedStateHandle.get<String>("folderDescription") ?: ""
        val folderIsPublic: Boolean = savedStateHandle.get<Boolean>("folderIsPublic") == true
        _uiState.update {
            it.copy(
                id = folderId,
                title = folderTitle,
                description = folderDescription,
                isPublic = folderIsPublic
            )
        }
    }

    fun onEvent(event: EditFolderUiAction) {
        when (event) {
            EditFolderUiAction.SaveClicked -> {
                when {
                    _uiState.value.title.isEmpty() -> {
                        _uiState.update { it.copy(titleError = R.string.txt_title_required) }
                    }

                    _uiState.value.title.length < 3 -> {
                        _uiState.update { it.copy(titleError = R.string.txt_title_must_be_at_least_1_characters) }
                    }

                    else -> {
                        _uiState.update { it.copy(titleError = null) }
                        saveFolder()
                    }
                }
            }

            is EditFolderUiAction.TitleChanged -> {
                _uiState.update {
                    it.copy(title = event.title)
                }
            }

            is EditFolderUiAction.DescriptionChanged -> {
                _uiState.update {
                    it.copy(description = event.description)
                }
            }

            is EditFolderUiAction.IsPublicChanged -> {
                _uiState.update {
                    it.copy(isPublic = event.isPublic)
                }
            }
        }
    }

    private fun saveFolder() {
        viewModelScope.launch {
            val updateFolderRequestModel = UpdateFolderRequestModel(
                title = uiState.value.title,
                description = uiState.value.description,
                isPublic = uiState.value.isPublic
            )
            folderRepository.updateFolder(
                folderId = uiState.value.id,
                updateFolderRequestModel = updateFolderRequestModel
            ).collectLatest { resource ->
                when (resource) {
                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        resource.data?.let {
                            _uiEvent.send(EditFolderUiEvent.FolderEdited)
                        } ?: run {
                            _uiEvent.send(EditFolderUiEvent.ShowError(R.string.txt_failed_to_update_folder))
                        }
                    }

                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.send(EditFolderUiEvent.ShowError(R.string.txt_failed_to_update_folder))
                    }
                }
            }
        }
    }
}