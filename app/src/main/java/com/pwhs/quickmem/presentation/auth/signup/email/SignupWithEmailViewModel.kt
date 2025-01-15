package com.pwhs.quickmem.presentation.auth.signup.email

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.AuthProvider
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.auth.SignupRequestModel
import com.pwhs.quickmem.domain.repository.AuthRepository
import com.pwhs.quickmem.utils.emailIsValid
import com.pwhs.quickmem.utils.getNameFromEmail
import com.pwhs.quickmem.utils.getUsernameFromEmail
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Random
import javax.inject.Inject

@HiltViewModel
class SignupWithEmailViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpWithEmailUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<SignUpWithEmailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SignUpWithEmailUiAction) {
        when (event) {
            is SignUpWithEmailUiAction.BirthdayChanged -> {
                if (event.birthday.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            birthday = event.birthday,
                            birthdayError = R.string.txt_birthday_is_required
                        )
                    }
                } else {
                    _uiState.update { it.copy(birthday = event.birthday, birthdayError = null) }
                }
            }

            is SignUpWithEmailUiAction.EmailChanged -> {
                if (!event.email.emailIsValid()) {
                    _uiState.update {
                        it.copy(
                            email = event.email,
                            emailError = R.string.txt_invalid_email
                        )
                    }
                } else {
                    _uiState.update { it.copy(email = event.email, emailError = null) }
                }
            }

            is SignUpWithEmailUiAction.PasswordChanged -> {
                if (event.password.length < 6) {
                    _uiState.update {
                        it.copy(
                            password = event.password,
                            passwordError = R.string.txt_password_is_too_weak_and_required
                        )
                    }
                } else {
                    _uiState.update { it.copy(password = event.password, passwordError = null) }
                }
            }

            is SignUpWithEmailUiAction.UserRoleChanged -> {
                _uiState.update { it.copy(userRole = event.userRole) }
            }

            is SignUpWithEmailUiAction.SignUp -> {
                if (validateInput()) {
                    signUp()
                } else {
                    _uiEvent.trySend(SignUpWithEmailUiEvent.SignUpFailure(R.string.txt_invalid_input))
                }
            }
        }
    }

    private fun signUp() {
        val tag = "SignupWithEmailViewModel"
        viewModelScope.launch {
            authRepository.checkEmailValidity(_uiState.value.email).collect { resource ->
                Log.d(tag, "Step 1: Checking email validity: ${_uiState.value.email}")
                when (resource) {
                    is Resources.Error -> {
                        Timber.e(resource.message)
                        _uiState.update {
                            it.copy(
                                emailError = R.string.txt_invalid_email_address,
                                isLoading = false
                            )
                        }
                        Log.e(tag, "Error check email: ${resource.message}")
                        _uiEvent.send(SignUpWithEmailUiEvent.SignUpFailure(R.string.txt_invalid_email_address))
                    }

                    is Resources.Loading -> {
                        Log.d(tag, "Loading: Checking email validity")
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resources.Success -> {
                        Log.d(tag, "Success: Checking email validity")
                        if (resource.data == true) {
                            Log.d(tag, "Email is valid")
                            val avatarUrl = Random().nextInt(126).toString()
                            val username = uiState.value.email.getUsernameFromEmail()
                            val fullName = uiState.value.email.getNameFromEmail()

                            authRepository.signup(
                                signUpRequestModel = SignupRequestModel(
                                    avatarUrl = avatarUrl,
                                    email = uiState.value.email,
                                    username = username,
                                    fullName = fullName,
                                    role = uiState.value.userRole,
                                    birthday = uiState.value.birthday,
                                    password = uiState.value.password,
                                    authProvider = AuthProvider.EMAIL.name
                                )
                            ).collectLatest { signup ->
                                Log.d(tag, "Step 2: Signing up")
                                when (signup) {
                                    is Resources.Error -> {
                                        Log.e(tag, "Error sign up: ${signup.message}")
                                        if (signup.status == 409 || signup.status == 412) {
                                            _uiState.update {
                                                it.copy(
                                                    emailError = R.string.txt_email_is_already_registered,
                                                    isLoading = false
                                                )
                                            }
                                            _uiEvent.send(SignUpWithEmailUiEvent.SignUpFailure(R.string.txt_email_is_already_registered))
                                        } else {
                                            _uiState.update {
                                                it.copy(
                                                    emailError = R.string.txt_something_went_wrong,
                                                    isLoading = false
                                                )
                                            }
                                            _uiEvent.send(SignUpWithEmailUiEvent.SignUpFailure(R.string.txt_something_went_wrong))
                                        }
                                    }

                                    is Resources.Loading -> {
                                        // Do nothing
                                        Log.d(tag, "Loading: Signing up")
                                    }

                                    is Resources.Success -> {
                                        Log.d(tag, "Success: Signing up")
                                        _uiState.update { it.copy(isLoading = false) }
                                        _uiEvent.send(SignUpWithEmailUiEvent.SignUpSuccess)
                                    }
                                }

                            }
                        } else {
                            Log.d(tag, "Email is invalid")
                            _uiState.update {
                                it.copy(
                                    emailError = R.string.txt_invalid_email_address,
                                    isLoading = false
                                )
                            }
                            _uiEvent.send(SignUpWithEmailUiEvent.SignUpFailure(R.string.txt_invalid_email_address))
                        }
                    }
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true

        if (!uiState.value.email.validEmail() || uiState.value.email.isEmpty()) {
            _uiState.update { it.copy(emailError = R.string.txt_invalid_email_address) }
            isValid = false
        } else {
            _uiState.update { it.copy(emailError = null) }
        }
        if (uiState.value.password.isEmpty() || uiState.value.password.length < 6) {
            _uiState.update { it.copy(passwordError = R.string.txt_password_is_too_weak_and_required) }
            isValid = false
        } else {
            _uiState.update { it.copy(passwordError = null) }
        }
        if (uiState.value.birthday.isEmpty()) {
            _uiState.update { it.copy(birthdayError = R.string.txt_birthday_is_required) }
            isValid = false
        } else {
            _uiState.update { it.copy(birthdayError = null) }
        }

        return isValid
    }

    override fun onCleared() {
        super.onCleared()
        _uiEvent.close()
    }
}