package com.pwhs.quickmem.presentation.app.settings.user_info.change_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.auth.ChangePasswordRequestModel
import com.pwhs.quickmem.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordSettingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChangePasswordSettingUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<ChangePasswordSettingUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ChangePasswordSettingUiAction) {
        when (event) {
            is ChangePasswordSettingUiAction.OnCurrentPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        currentPassword = event.currentPassword,
                        errorCurrentPassword = null
                    )
                }
            }

            is ChangePasswordSettingUiAction.OnNewPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        newPassword = event.newPassword,
                        errorNewPassword = null
                    )
                }
            }

            is ChangePasswordSettingUiAction.OnConfirmPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        confirmPassword = event.confirmPassword,
                        errorConfirmPassword = null
                    )
                }
            }

            is ChangePasswordSettingUiAction.OnSaveClicked -> {
                if (validatePassword(
                        _uiState.value.newPassword,
                        _uiState.value.confirmPassword,
                        _uiState.value.currentPassword
                    )
                ) {
                    changePassword()
                } else {
                    _uiEvent.trySend(ChangePasswordSettingUiEvent.OnError(R.string.txt_error_occurred))
                }
            }
        }
    }

    private fun changePassword() {
        viewModelScope.launch {
            val currentPassword = _uiState.value.currentPassword
            val newPassword = _uiState.value.newPassword

            authRepository.changePassword(
                ChangePasswordRequestModel(
                    oldPassword = currentPassword,
                    newPassword = newPassword
                )
            ).collect { resource ->
                when (resource) {
                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(
                                errorCurrentPassword = R.string.txt_error_occurred,
                                isLoading = false
                            )
                        }
                        _uiEvent.send(
                            ChangePasswordSettingUiEvent.OnError(R.string.txt_error_occurred)
                        )
                    }

                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.send(ChangePasswordSettingUiEvent.OnPasswordChanged)
                    }
                }
            }
        }
    }

    private fun validatePassword(
        newPassword: String,
        confirmPassword: String,
        currentPassword: String,
    ): Boolean {
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            _uiState.update {
                it.copy(
                    errorCurrentPassword = R.string.txt_please_fill_in_all_fields,
                    errorNewPassword = R.string.txt_please_fill_in_all_fields,
                    errorConfirmPassword = R.string.txt_please_fill_in_all_fields,
                    isLoading = false
                )
            }
            return false
        }

        if (newPassword.length < 6) {
            _uiState.update {
                it.copy(
                    errorNewPassword = R.string.txt_password_is_too_weak_and_required,
                    isLoading = false
                )
            }
            return false
        }

        if (newPassword != confirmPassword) {
            _uiState.update {
                it.copy(
                    errorNewPassword = R.string.txt_passwords_do_not_match,
                    errorConfirmPassword = R.string.txt_passwords_do_not_match,
                    isLoading = false
                )
            }
            return false
        }

        if (currentPassword == newPassword) {
            _uiState.update {
                it.copy(
                    errorNewPassword = R.string.txt_new_password_must_be_different_from_the_current_password,
                    isLoading = false
                )
            }
            return false
        }
        return true
    }
}
