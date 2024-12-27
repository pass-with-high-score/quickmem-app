package com.pwhs.quickmem.presentation.app.deeplink.classes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.classes.JoinClassRequestModel
import com.pwhs.quickmem.domain.repository.ClassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class JoinClassViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tokenManager: TokenManager,
    private val appManager: AppManager,
    private val classRepository: ClassRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(JoinClassUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<JoinClassUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val code = savedStateHandle.get<String>("code") ?: ""
        val isFromDeepLink = savedStateHandle.get<Boolean>("isFromDeepLink") == true
        _uiState.update {
            it.copy(
                code = code,
                isFromDeepLink = isFromDeepLink
            )
        }

        getClassByCode()
    }

    fun onEvent(event: JoinClassUiAction) {
        when (event) {
            JoinClassUiAction.JoinClass -> joinClass()
        }
    }

    private fun getClassByCode() {
        viewModelScope.launch {
            val classCode = uiState.value.code ?: ""
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            val userId = appManager.userId.firstOrNull() ?: ""
            if (classCode.isEmpty() || token.isEmpty() || userId.isEmpty()) {
                _uiEvent.send(JoinClassUiEvent.UnAuthorized)
            }

            classRepository.getClassByCode(
                token = token,
                userId = userId,
                classCode = classCode
            ).collectLatest { resource ->
                when (resource) {
                    is Resources.Error -> {
                        Timber.e(resource.message)
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.send(JoinClassUiEvent.NotFound)
                    }

                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resources.Success -> {
                        if (resource.data?.owner?.id == userId || resource.data?.isJoined == true) {
                            _uiState.update {
                                it.copy(
                                    classDetailResponseModel = resource.data,
                                    isLoading = false,
                                    userId = userId,
                                    classId = resource.data.id
                                )
                            }
                            _uiEvent.send(
                                JoinClassUiEvent.JoinedClass(
                                    id = resource.data.id,
                                    classCode = classCode,
                                    title = resource.data.title,
                                    description = resource.data.description
                                )
                            )
                        } else {
                            _uiState.update {
                                it.copy(
                                    classDetailResponseModel = resource.data,
                                    isLoading = false,
                                    userId = userId,
                                    classId = resource.data?.id
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun joinClass() {
        viewModelScope.launch {
            val classCode = uiState.value.code ?: return@launch
            val token = tokenManager.accessToken.firstOrNull() ?: return@launch

            val joinClassRequestModel = JoinClassRequestModel(
                joinToken = classCode,
                userId = uiState.value.userId ?: "",
                classId = uiState.value.classId ?: "",
            )

            classRepository.joinClass(token, joinClassRequestModel).collectLatest { resource ->
                when (resource) {
                    is Resources.Error -> {
                        Timber.e(resource.message)
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resources.Success -> {
                        _uiEvent.send(
                            JoinClassUiEvent.JoinedClass(
                                id = _uiState.value.classId ?: "",
                                classCode = _uiState.value.code ?: "",
                                title = _uiState.value.classDetailResponseModel?.title ?: "",
                                description = _uiState.value.classDetailResponseModel?.description
                                    ?: ""
                            )
                        )
                    }
                }
            }
        }
    }
}