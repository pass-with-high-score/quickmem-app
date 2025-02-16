package com.pwhs.quickmem.presentation.app.settings.user_info.email

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.auth.UpdateEmailRequestModel
import com.pwhs.quickmem.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateEmailSettingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tokenManager: TokenManager,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UpdateEmailSettingUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UpdateEmailSettingUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val userId = savedStateHandle.get<String>("userId") ?: ""
        val email = savedStateHandle.get<String>("email") ?: ""
        _uiState.update {
            it.copy(
                id = userId,
                email = email
            )
        }
    }

    fun onEvent(event: UpdateEmailSettingUiAction) {
        when (event) {
            is UpdateEmailSettingUiAction.OnEmailChanged -> {
                _uiState.update {
                    it.copy(
                        email = event.email,
                        errorMessage = ""
                    )
                }
            }

            is UpdateEmailSettingUiAction.OnSaveClicked -> {
                saveEmail()
            }
        }
    }

    private fun saveEmail() {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            val userId = _uiState.value.id
            val email = _uiState.value.email
            authRepository.updateEmail(
                token,
                UpdateEmailRequestModel(userId = userId, email = email)
            ).collect { resource ->
                when (resource) {
                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = "An error occurred",
                                isLoading = false
                            )
                        }
                        _uiEvent.send(
                            UpdateEmailSettingUiEvent.OnError(
                                resource.message ?: "An error occurred"
                            )
                        )
                    }

                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resources.Success -> {
                        _uiEvent.send(UpdateEmailSettingUiEvent.OnEmailChanged)
                    }
                }
            }
        }
    }
}
