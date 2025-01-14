package com.pwhs.quickmem.presentation.auth.login.email

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.AuthProvider
import com.pwhs.quickmem.core.data.enums.UserStatus
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.auth.LoginRequestModel
import com.pwhs.quickmem.domain.model.auth.ResendEmailRequestModel
import com.pwhs.quickmem.domain.repository.AuthRepository
import com.pwhs.quickmem.utils.emailIsValid
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.interfaces.LogInCallback
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
import javax.inject.Inject

@HiltViewModel
class LoginWithEmailViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val appManager: AppManager,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginWithEmailUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<LoginWithEmailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val email = savedStateHandle.get<String>("email")
        if (!email.isNullOrEmpty()) {
            _uiState.update { it.copy(email = email) }
        }
    }

    fun onEvent(event: LoginWithEmailUiAction) {
        when (event) {
            is LoginWithEmailUiAction.EmailChanged -> {
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

            is LoginWithEmailUiAction.PasswordChanged -> {
                if (event.password.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            password = event.password,
                            passwordError = R.string.txt_password_is_required
                        )
                    }
                } else {
                    _uiState.update { it.copy(password = event.password, passwordError = null) }
                }
            }

            is LoginWithEmailUiAction.Login -> {
                if (validateInput()) {
                    login()
                } else {
                    _uiEvent.trySend(LoginWithEmailUiEvent.LoginFailure(R.string.txt_invalid_input))
                }
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            val email = uiState.value.email
            authRepository.checkEmailValidity(email).collect { resource ->
                when (resource) {
                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(
                                emailError = R.string.txt_invalid_email_address,
                                isLoading = false
                            )
                        }
                        _uiEvent.send(LoginWithEmailUiEvent.LoginFailure(R.string.txt_invalid_email_address))
                    }

                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resources.Success -> {
                        if (resource.data == true) {
                            val password = uiState.value.password
                            val provider = AuthProvider.EMAIL.name
                            val loginRequestModel =
                                LoginRequestModel(email, password, provider, null)
                            authRepository.login(loginRequestModel).collectLatest { login ->
                                when (login) {
                                    is Resources.Error -> {
                                        if (login.status == 401) {
                                            _uiState.update {
                                                it.copy(
                                                    emailError = R.string.txt_invalid_email_or_password,
                                                    passwordError = R.string.txt_invalid_email_or_password,
                                                    isLoading = false
                                                )
                                            }
                                        } else if (login.status == 404) {
                                            _uiState.update {
                                                it.copy(
                                                    emailError = R.string.txt_email_is_not_registered,
                                                    isLoading = false
                                                )
                                            }
                                        } else {
                                            _uiState.update {
                                                it.copy(
                                                    emailError = R.string.txt_error_occurred,
                                                    isLoading = false
                                                )
                                            }
                                        }
                                    }

                                    is Resources.Loading -> {
                                        // Show loading
                                    }

                                    is Resources.Success -> {
                                        if (login.data?.isVerified == false) {
                                            resendVerificationEmail()
                                        } else if (login.data?.userStatus == UserStatus.BLOCKED.status) {
                                            _uiState.update {
                                                it.copy(
                                                    emailError = R.string.txt_your_account_has_been_blocked,
                                                    isLoading = false
                                                )
                                            }
                                        } else {
                                            tokenManager.saveAccessToken(
                                                login.data?.accessToken ?: ""
                                            )
                                            tokenManager.saveRefreshToken(
                                                login.data?.refreshToken ?: ""
                                            )
                                            appManager.saveIsLoggedIn(true)
                                            appManager.saveUserId(login.data?.id ?: "")
                                            appManager.saveUserAvatar(login.data?.avatarUrl ?: "")
                                            appManager.saveUserFullName(login.data?.fullName ?: "")
                                            appManager.saveUserEmail(login.data?.email ?: "")
                                            appManager.saveUserBirthday(login.data?.birthday ?: "")
                                            appManager.saveUserName(login.data?.username ?: "")
                                            appManager.saveUserRole(login.data?.role ?: "")
                                            appManager.saveUserCoins(login.data?.coin ?: 0)
                                            appManager.saveUserLoginProviders(
                                                login.data?.provider ?: emptyList()
                                            )
                                            Purchases.sharedInstance.apply {
                                                setEmail(login.data?.email)
                                                setDisplayName(login.data?.fullName)
                                                logIn(
                                                    newAppUserID = login.data?.id ?: "",
                                                    callback = object : LogInCallback {
                                                        override fun onError(error: PurchasesError) {
                                                            Timber.e(error.message)
                                                        }

                                                        override fun onReceived(
                                                            customerInfo: CustomerInfo,
                                                            created: Boolean,
                                                        ) {
                                                            Timber.d("Customer info: $customerInfo")
                                                        }

                                                    }
                                                )
                                            }
                                            _uiState.update { it.copy(isLoading = false) }
                                            _uiEvent.trySend(LoginWithEmailUiEvent.LoginSuccess)
                                        }
                                    }
                                }
                            }
                        } else {
                            _uiState.update {
                                it.copy(
                                    emailError = R.string.txt_invalid_email_address,
                                    isLoading = false
                                )
                            }
                            _uiEvent.send(LoginWithEmailUiEvent.LoginFailure(R.string.txt_invalid_email_address))
                        }
                    }
                }
            }
        }
    }


    private fun resendVerificationEmail() {
        viewModelScope.launch {
            val email = uiState.value.email
            authRepository.resendOtp(
                ResendEmailRequestModel(
                    email = email
                )
            ).collectLatest { resource ->
                when (resource) {
                    is Resources.Loading -> {
                        // Show loading
                    }

                    is Resources.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEvent.send(LoginWithEmailUiEvent.NavigateToVerifyEmail(email))
                    }

                    is Resources.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEvent.send(LoginWithEmailUiEvent.LoginFailure(R.string.txt_error_occurred))
                    }
                }
            }
        }
    }


    private fun validateInput(): Boolean {
        var isValid = true

        if (!uiState.value.email.validEmail() || uiState.value.email.isEmpty()) {
            _uiState.update { it.copy(emailError = R.string.txt_invalid_email) }
            isValid = false
        } else {
            _uiState.update { it.copy(emailError = null) }
        }
        if (uiState.value.password.length < 6 || uiState.value.password.isEmpty()) {
            _uiState.update { it.copy(passwordError = R.string.txt_password_is_too_weak_and_required) }
            isValid = false
        } else {
            _uiState.update { it.copy(passwordError = null) }
        }

        return isValid
    }
}
