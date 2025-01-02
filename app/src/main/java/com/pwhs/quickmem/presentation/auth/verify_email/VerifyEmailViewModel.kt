package com.pwhs.quickmem.presentation.auth.verify_email

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.auth.ResendEmailRequestModel
import com.pwhs.quickmem.domain.model.auth.VerifyEmailResponseModel
import com.pwhs.quickmem.domain.repository.AuthRepository
import com.pwhs.quickmem.utils.emailIsValid
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.interfaces.LogInCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    stateHandle: SavedStateHandle,
    private val tokenManager: TokenManager,
    private val appManager: AppManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(VerifyEmailUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<VerifyEmailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val email = stateHandle.get<String>("email") ?: ""
        val isFromSignup = stateHandle.get<Boolean>("isFromSignup") == true
        val resetPasswordToken = stateHandle.get<String>("resetPasswordToken") ?: ""
        _uiState.update {
            it.copy(
                email = email,
                isFromSignup = isFromSignup,
                resetPasswordToken = resetPasswordToken
            )
        }
        updateCountdown()
    }

    fun onEvent(event: VerifyEmailUiAction) {
        when (event) {
            is VerifyEmailUiAction.EmailChange -> {
                if (!event.email.emailIsValid()) {
                    _uiState.update { it.copy(email = event.email) }
                } else {
                    _uiState.update { it.copy(email = event.email) }
                }
            }

            is VerifyEmailUiAction.OtpChange -> {
                _uiState.update { it.copy(otp = event.otp) }
            }

            is VerifyEmailUiAction.VerifyEmail -> {
                verifyEmail()
            }

            is VerifyEmailUiAction.ResendEmail -> {
                _uiState.update { it.copy(countdown = 60) }
                resendOtp(uiState.value.email)
            }
        }
    }

    private fun updateCountdown() {
        viewModelScope.launch {
            _uiState.update { it.copy(countdown = 60) }
            while (_uiState.value.countdown > 0) {
                delay(1000)
                _uiState.update { it.copy(countdown = _uiState.value.countdown - 1) }
            }
        }
    }

    private fun verifyEmail() {
        viewModelScope.launch {
            val email = uiState.value.email
            val otp = uiState.value.otp

            if (email.isEmpty() || otp.isEmpty()) {
                _uiEvent.send(VerifyEmailUiEvent.EmptyOtp)
                return@launch
            }

            if (!isOtpLengthValid(otp)) {
                _uiEvent.send(VerifyEmailUiEvent.ErrorLengthOtp)
                return@launch
            }

            if (!isOtpNumber(otp)) {
                _uiEvent.send(VerifyEmailUiEvent.WrongOtp)
                return@launch
            }
            if (_uiState.value.isFromSignup) {
                authRepository.verifyEmail(
                    VerifyEmailResponseModel(
                        email = email,
                        otp = otp
                    )
                ).collectLatest { resource ->
                    when (resource) {
                        is Resources.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }

                        is Resources.Success -> {
                            tokenManager.saveAccessToken(resource.data?.accessToken ?: "")
                            tokenManager.saveRefreshToken(resource.data?.refreshToken ?: "")
                            appManager.saveUserAvatar(resource.data?.avatarUrl ?: "")
                            appManager.saveUserFullName(resource.data?.fullName ?: "")
                            appManager.saveUserName(resource.data?.username ?: "")
                            appManager.saveUserId(resource.data?.id ?: "")
                            appManager.saveUserRole(resource.data?.role ?: "")
                            appManager.saveUserLoginProviders(
                                resource.data?.provider ?: emptyList()
                            )
                            appManager.saveIsLoggedIn(true)
                            _uiState.update { it.copy(isLoading = false) }
                            Purchases.sharedInstance.apply {
                                setEmail(resource.data?.email)
                                setDisplayName(resource.data?.fullName)
                                logIn(
                                    newAppUserID = resource.data?.id ?: "",
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
                            _uiEvent.send(VerifyEmailUiEvent.VerifySuccess)
                        }

                        is Resources.Error -> {
                            Timber.e(resource.message)
                            _uiState.update { it.copy(isLoading = false) }
                            _uiEvent.send(VerifyEmailUiEvent.VerifyFailure)
                        }
                    }
                }
            } else {
                _uiEvent.send(VerifyEmailUiEvent.NavigateToSetNewPassword)
            }
        }
    }

    private fun resendOtp(email: String) {
        viewModelScope.launch {
            val response = authRepository.resendOtp(
                ResendEmailRequestModel(
                    email = email
                )
            )

            response.collectLatest { resource ->
                when (resource) {
                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resources.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEvent.send(VerifyEmailUiEvent.ResendSuccess)
                    }

                    is Resources.Error -> {
                        Timber.e(resource.message)
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEvent.send(VerifyEmailUiEvent.ResendFailure)
                    }
                }
            }
        }
    }

    private fun isOtpNumber(otp: String): Boolean {
        return otp.all { it.isDigit() }
    }

    private fun isOtpLengthValid(otp: String): Boolean {
        return otp.length == 6
    }

}