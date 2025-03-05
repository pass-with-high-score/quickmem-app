package com.pwhs.quickmem.presentation.app.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.repository.FolderRepository
import com.pwhs.quickmem.domain.repository.StudySetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val studySetRepository: StudySetRepository,
    private val folderRepository: FolderRepository,
    private val appManager: AppManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<LibraryUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var job: Job? = null

    init {
        initData()
    }

    private fun initData() {
        viewModelScope.launch {
            val userId = appManager.userId.firstOrNull() ?: ""
            if (userId.isNotEmpty()) {
                getUserInfo()
                getStudySets()
                getFolders()
            }
        }
    }

    fun onEvent(event: LibraryUiAction) {
        when (event) {
            is LibraryUiAction.Refresh -> {
                initData()
            }

            LibraryUiAction.RefreshStudySets -> {
                job?.cancel()
                job = viewModelScope.launch {
                    delay(500)
                    getStudySets()
                }
            }

            LibraryUiAction.RefreshFolders -> {
                job?.cancel()
                job = viewModelScope.launch {
                    delay(500)
                    getFolders()
                }
            }
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            val username = appManager.username.firstOrNull() ?: ""
            val userAvatarUrl = appManager.userAvatarUrl.firstOrNull() ?: ""
            _uiState.update {
                it.copy(
                    username = username,
                    userAvatarUrl = userAvatarUrl
                )
            }
        }
    }

    private fun getStudySets() {
        viewModelScope.launch {
            studySetRepository.getStudySetsByOwnerId(classId = null, folderId = null)
                .collectLatest { resources ->
                    when (resources) {
                        is Resources.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    studySets = resources.data ?: emptyList(),
                                )
                            }
                        }

                        is Resources.Error -> {
                            _uiState.update {
                                it.copy(isLoading = false)
                            }
                            _uiEvent.send(
                                LibraryUiEvent.Error(
                                    resources.message ?: "An error occurred"
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
            folderRepository.getFoldersByUserId(classId = null, studySetId = null)
                .collectLatest { resources ->
                    when (resources) {
                        is Resources.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    folders = resources.data ?: emptyList(),
                                )
                            }
                        }

                        is Resources.Error -> {
                            _uiState.update {
                                it.copy(isLoading = false)
                            }
                            _uiEvent.send(
                                LibraryUiEvent.Error(
                                    resources.message ?: "An error occurred"
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
}