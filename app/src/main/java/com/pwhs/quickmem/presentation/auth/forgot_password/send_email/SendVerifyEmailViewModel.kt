package com.pwhs.quickmem.presentation.auth.forgot_password.send_email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.auth.SendResetPasswordRequestModel
import com.pwhs.quickmem.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SendVerifyEmailViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SendVerifyEmailUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<SendVerifyEmailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SendVerifyEmailUiAction) {
        when (event) {
            is SendVerifyEmailUiAction.EmailChangedAction -> {
                _uiState.update {
                    it.copy(
                        email = event.email,
                        emailError = if (android.util.Patterns.EMAIL_ADDRESS.matcher(event.email)
                                .matches()
                        ) null
                        else R.string.txt_invalid_email_address
                    )
                }
            }

            is SendVerifyEmailUiAction.ResetPassword -> {
                if (validateInput()) {
                    resetPassword()
                } else {
                    _uiEvent.trySend(SendVerifyEmailUiEvent.SendEmailFailure(R.string.txt_invalid_input))
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        val emailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(uiState.value.email).matches()
        _uiState.update { it.copy(emailError = if (emailValid) null else R.string.txt_invalid_email_address) }
        return emailValid
    }

    private fun resetPassword() {
        viewModelScope.launch {
            authRepository.sendResetPassword(SendResetPasswordRequestModel(_uiState.value.email))
                .collect { resource ->
                    when (resource) {
                        is Resources.Error -> {
                            Timber.e(resource.message ?: "Unknown error")
                            _uiState.update { it.copy(isLoading = false) }
                            _uiEvent.send(SendVerifyEmailUiEvent.SendEmailFailure(R.string.txt_error_occurred))
                        }

                        is Resources.Loading -> {
                            // Do nothing
                        }

                        is Resources.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    resetPasswordToken = resource.data?.resetPasswordToken
                                        ?: ""
                                )
                            }
                            _uiEvent.trySend(SendVerifyEmailUiEvent.SendEmailSuccess)
                        }
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _uiEvent.close()
    }
}

