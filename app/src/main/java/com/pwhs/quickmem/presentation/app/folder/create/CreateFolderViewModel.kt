package com.pwhs.quickmem.presentation.app.folder.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.folder.CreateFolderRequestModel
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
class CreateFolderViewModel @Inject constructor(
    private val folderRepository: FolderRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateFolderUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<CreateFolderUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: CreateFolderUiAction) {
        when (event) {
            is CreateFolderUiAction.TitleChanged -> {
                _uiState.update {
                    it.copy(title = event.title)
                }
            }

            is CreateFolderUiAction.DescriptionChanged -> {
                _uiState.update {
                    it.copy(description = event.description)
                }
            }

            is CreateFolderUiAction.PublicChanged -> {
                _uiState.update {
                    it.copy(isPublic = event.isPublic)
                }
            }

            is CreateFolderUiAction.SaveClicked -> {
                val uiState = _uiState.value
                if (uiState.title.isEmpty()) {
                    _uiState.update {
                        it.copy(titleError = R.string.txt_title_required)
                    }
                    return
                } else {
                    _uiState.update {
                        it.copy(titleError = null)
                    }
                }
                viewModelScope.launch {
                    val createFolderRequestModel = CreateFolderRequestModel(
                        title = uiState.title,
                        description = uiState.description,
                        isPublic = uiState.isPublic,
                    )
                    folderRepository.createFolder(
                        createFolderRequestModel = createFolderRequestModel
                    ).collectLatest { resources ->
                        when (resources) {
                            is Resources.Loading -> {
                                _uiState.update {
                                    it.copy(isLoading = true)
                                }
                            }

                            is Resources.Success -> {
                                _uiState.update {
                                    it.copy(isLoading = false)
                                }
                                _uiEvent.send(
                                    CreateFolderUiEvent.FolderCreated(
                                        resources.data?.id ?: ""
                                    )
                                )
                            }

                            is Resources.Error -> {
                                _uiState.update {
                                    it.copy(isLoading = false)
                                }
                                _uiEvent.send(CreateFolderUiEvent.ShowError(R.string.txt_error_create_folder))
                            }
                        }
                    }
                }
            }
        }
    }
}