package com.pwhs.quickmem.presentation.app.profile.change_avatar

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.auth.UpdateAvatarRequestModel
import com.pwhs.quickmem.domain.model.users.AvatarResponseModel
import com.pwhs.quickmem.domain.repository.AuthRepository
import com.pwhs.quickmem.domain.repository.UploadImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeAvatarViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val uploadImageRepository: UploadImageRepository,
    private val appManager: AppManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangeAvatarUiState())
    val uiState: StateFlow<ChangeAvatarUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ChangeAvatarUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getCurrentAvatar()
        getListAvatar()
    }

    private fun getCurrentAvatar() {
        viewModelScope.launch {
            val currentAvatarUrl = appManager.userAvatarUrl.firstOrNull()
            if (currentAvatarUrl != null) {
                _uiState.update {
                    it.copy(
                        selectedAvatarUrl = currentAvatarUrl
                    )
                }
            }
        }
    }

    fun onEvent(event: ChangeAvatarUiAction) {
        when (event) {
            is ChangeAvatarUiAction.ImageSelected -> {
                _uiState.update {
                    it.copy(
                        selectedAvatarUrl = event.avatarUrl
                    )
                }
            }

            is ChangeAvatarUiAction.SaveClicked -> {
                val selectedAvatarUrl = _uiState.value.selectedAvatarUrl
                if (selectedAvatarUrl != null) {
                    val avatarUri = _uiState.value.avatarUri
                    if (avatarUri != null) {
                        uploadAvatar(avatarUri)
                    } else {
                        updateAvatarUrl(selectedAvatarUrl)
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }

            is ChangeAvatarUiAction.OnImageUriChanged -> {
                _uiState.update {
                    it.copy(
                        avatarUri = event.imageUri,
                        selectedAvatarUrl = event.imageUri.toString(),
                        // put image uri to first list
                        avatarUrls = listOf(
                            AvatarResponseModel(
                                url = event.imageUri.toString()
                            )
                        ) + it.avatarUrls
                    )
                }
            }
        }
    }

    private fun getListAvatar() {
        viewModelScope.launch {
            val userAvatarUrl = appManager.userAvatarUrl.firstOrNull() ?: ""
            authRepository.getAvatar().collect { resource ->
                when (resource) {
                    is Resources.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }

                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resources.Success -> {
                        val avatarUrls = resource.data ?: emptyList()
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                avatarUrls = listOf(
                                    AvatarResponseModel(
                                        url = userAvatarUrl
                                    )
                                ) + avatarUrls.filter { it.url != userAvatarUrl },
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateAvatarUrl(selectAvatarUrl: String) {
        viewModelScope.launch {
            val userAvatarUrl = appManager.userAvatarUrl.firstOrNull() ?: ""

            if (userAvatarUrl == selectAvatarUrl) {
                _uiEvent.send(
                    ChangeAvatarUiEvent.AvatarUpdated(_uiState.value.selectedAvatarUrl ?: "")
                )
                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
                return@launch
            }

            authRepository.updateAvatar(
                updateAvatarRequestModel = UpdateAvatarRequestModel(selectAvatarUrl)
            ).collect { resource ->
                when (resource) {
                    is Resources.Error -> {
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
                        val newAvatarUrl = resource.data?.avatarUrl ?: ""
                        appManager.saveUserAvatar(newAvatarUrl)
                        _uiEvent.send(
                            ChangeAvatarUiEvent.AvatarUpdated(newAvatarUrl)
                        )
                        _uiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun uploadAvatar(imageUri: Uri) {
        viewModelScope.launch {
            uploadImageRepository.uploadUserAvatar(
                imageUri = imageUri,
            ).collect { resource ->
                when (resource) {
                    is Resources.Error -> {
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
                        val newAvatarUrl = resource.data?.url ?: ""
                        appManager.saveUserAvatar(newAvatarUrl)
                        _uiEvent.send(
                            ChangeAvatarUiEvent.AvatarUpdated(newAvatarUrl)
                        )
                        _uiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
}
