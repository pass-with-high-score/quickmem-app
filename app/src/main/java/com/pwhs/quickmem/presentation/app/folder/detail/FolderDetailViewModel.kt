package com.pwhs.quickmem.presentation.app.folder.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.LearnMode
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.folder.SaveRecentAccessFolderRequestModel
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.domain.repository.FolderRepository
import com.pwhs.quickmem.presentation.app.folder.detail.FolderDetailUiEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FolderDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tokenManager: TokenManager,
    private val appManager: AppManager,
    private val folderRepository: FolderRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FolderDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<FolderDetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var job: Job? = null

    init {
        val id: String = savedStateHandle.get<String>("id") ?: ""
        val code: String = savedStateHandle.get<String>("code") ?: ""
        _uiState.update {
            it.copy(
                id = id,
                linkShareCode = code
            )
        }
        getFolderSetById(isRefresh = false)
    }

    fun onEvent(event: FolderDetailUiAction) {
        when (event) {
            is FolderDetailUiAction.Refresh -> {
                job?.cancel()
                job = viewModelScope.launch {
                    delay(500)
                    getFolderSetById(isRefresh = true)
                }
            }

            is FolderDetailUiAction.DeleteFolder -> {
                if (_uiState.value.isOwner) {
                    _uiState.update { it.copy(isLoading = true) }
                    deleteFolder()
                } else {
                    _uiEvent.trySend(ShowError(R.string.txt_you_can_t_delete_this_folder))
                }
            }

            is FolderDetailUiAction.EditFolder -> {
                if (_uiState.value.isOwner) {
                    _uiEvent.trySend(NavigateToEditFolder)
                } else {
                    _uiEvent.trySend(ShowError(R.string.txt_you_can_t_edit_this_folder))
                }
            }

            is FolderDetailUiAction.NavigateToLearn -> {
                when (event.learnMode) {
                    LearnMode.FLIP -> {
                        _uiEvent.trySend(OnNavigateToFlipFlashcard(event.isGetAll))
                    }

                    LearnMode.QUIZ -> {
                        _uiEvent.trySend(OnNavigateToQuiz(event.isGetAll))
                    }

                    LearnMode.TRUE_FALSE -> {
                        _uiEvent.trySend(OnNavigateToTrueFalse(event.isGetAll))
                    }

                    LearnMode.WRITE -> {
                        _uiEvent.trySend(OnNavigateToWrite(event.isGetAll))
                    }

                    else -> {
                        // Do nothing
                    }
                }
            }
        }
    }

    private fun getFolderSetById(isRefresh: Boolean = false) {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            val code = _uiState.value.linkShareCode
            val id = _uiState.value.id
            if (token.isEmpty()) {
                _uiEvent.send(UnAuthorized)
                return@launch
            }
            if (code.isNotEmpty()) {
                folderRepository.getFolderByLinkCode(token = token, code = code)
                    .collect { resource ->
                        when (resource) {
                            is Resources.Error -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false
                                    )
                                }
                                _uiEvent.send(NotFound)
                            }

                            is Resources.Loading -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = true
                                    )
                                }
                            }

                            is Resources.Success -> {
                                resource.data?.let { data ->
                                    val isOwner = appManager.userId.firstOrNull() == data.owner.id
                                    val totalFlashCards =
                                        calculateTotalFlashCards(data.studySets ?: emptyList())
                                    _uiState.update {
                                        it.copy(
                                            title = data.title,
                                            linkShareCode = data.linkShareCode ?: "",
                                            description = data.description,
                                            isPublic = data.isPublic,
                                            studySetCount = data.studySetCount,
                                            user = data.owner,
                                            studySets = data.studySets ?: emptyList(),
                                            createdAt = data.createdAt,
                                            updatedAt = data.updatedAt,
                                            isLoading = false,
                                            isOwner = isOwner,
                                            totalFlashCards = totalFlashCards
                                        )
                                    }
                                }
                                if (!isRefresh && _uiState.value.id.isNotEmpty()) {
                                    saveRecentAccessFolder()
                                }
                            }
                        }
                    }
            } else {
                folderRepository.getFolderById(
                    token = token,
                    folderId = id
                ).collectLatest { resource ->
                    when (resource) {
                        is Resources.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }

                        is Resources.Success -> {
                            resource.data?.let { data ->
                                val isOwner = appManager.userId.firstOrNull() == data.owner.id
                                val totalFlashCards =
                                    calculateTotalFlashCards(data.studySets ?: emptyList())
                                _uiState.update {
                                    it.copy(
                                        title = data.title,
                                        linkShareCode = data.linkShareCode ?: "",
                                        description = data.description,
                                        isPublic = data.isPublic,
                                        studySetCount = data.studySetCount,
                                        user = data.owner,
                                        studySets = data.studySets ?: emptyList(),
                                        createdAt = data.createdAt,
                                        updatedAt = data.updatedAt,
                                        isLoading = false,
                                        isOwner = isOwner,
                                        totalFlashCards = totalFlashCards
                                    )
                                }
                            }
                            if (!isRefresh) {
                                saveRecentAccessFolder()
                            }
                        }

                        is Resources.Error -> {
                            Timber.e(resource.message)
                            _uiState.update { it.copy(isLoading = false) }
                            _uiEvent.send(NotFound)
                        }
                    }
                }
            }
        }
    }

    private fun deleteFolder() {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: run {
                _uiEvent.send(ShowError(R.string.txt_please_login_again))
                return@launch
            }
            folderRepository.deleteFolder(token, _uiState.value.id).collectLatest { resource ->
                when (resource) {
                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resources.Success -> {
                        resource.data?.let {
                            _uiState.update { it.copy(isLoading = false) }
                            _uiEvent.send(FolderDeleted)
                        }
                    }

                    is Resources.Error -> {
                        Timber.e(resource.message)
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }
                }
            }
        }
    }

    private fun saveRecentAccessFolder() {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            val userId = appManager.userId.firstOrNull() ?: ""
            val folderId = _uiState.value.id
            val saveRecentAccessFolderRequestModel = SaveRecentAccessFolderRequestModel(
                userId = userId,
                folderId = folderId
            )
            folderRepository.saveRecentAccessFolder(token, saveRecentAccessFolderRequestModel)
                .collect()
        }
    }

    private fun calculateTotalFlashCards(studySets: List<GetStudySetResponseModel>): Int {
        var totalFlashCards = 0
        studySets.forEach { studySet ->
            totalFlashCards += studySet.flashcardCount
        }
        return totalFlashCards
    }
}