package com.pwhs.quickmem.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.UserStatus
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.auth.AuthSocialGoogleRequestModel
import com.pwhs.quickmem.domain.repository.AuthRepository
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.interfaces.LogInCallback
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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val appManager: AppManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<LoginUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: LoginUiAction) {
        when (event) {
            is LoginUiAction.LoginWithGoogle -> loginWithGoogle(event.authSocialGoogleRequestModel)
            is LoginUiAction.LoginWithEmail -> loginWithEmail()
            is LoginUiAction.NavigateToSignUp -> navigateToSignUp()
        }
    }

    private fun loginWithGoogle(authSocialGoogleRequestModel: AuthSocialGoogleRequestModel) {
        viewModelScope.launch {
            authRepository.loginWithGoogle(
                authSocialGoogleRequestModel = authSocialGoogleRequestModel,
            ).collect { resource ->
                when (resource) {
                    is Resources.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        if (resource.status == 409) {
                            _uiEvent.trySend(LoginUiEvent.NavigateToLoginWithEmail(email = authSocialGoogleRequestModel.email))
                        } else {
                            _uiEvent.trySend(LoginUiEvent.LoginFailure(authSocialGoogleRequestModel))
                        }
                    }

                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resources.Success -> {
                        if (resource.data?.userStatus == UserStatus.BLOCKED.status) {
                            _uiState.update {
                                it.copy(
                                    error = R.string.txt_your_account_has_been_blocked,
                                    isLoading = false
                                )
                            }
                            _uiEvent.trySend(LoginUiEvent.ShowError(R.string.txt_your_account_has_been_blocked))
                        } else {
                            tokenManager.saveAccessToken(
                                resource.data?.accessToken ?: ""
                            )
                            tokenManager.saveRefreshToken(
                                resource.data?.refreshToken ?: ""
                            )
                            appManager.saveIsLoggedIn(true)
                            appManager.saveUserId(resource.data?.id ?: "")
                            appManager.saveUserAvatar(resource.data?.avatarUrl ?: "")
                            appManager.saveUserFullName(resource.data?.fullName ?: "")
                            appManager.saveUserEmail(resource.data?.email ?: "")
                            appManager.saveUserBirthday(resource.data?.birthday ?: "")
                            appManager.saveUserName(resource.data?.username ?: "")
                            appManager.saveUserCoins(resource.data?.coin ?: 0)
                            appManager.saveUserLoginProviders(resource.data?.provider ?: emptyList())
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
                            _uiState.update { it.copy(isLoading = false) }
                            _uiEvent.trySend(LoginUiEvent.LoginSuccess)
                        }
                    }
                }

            }
        }
    }

    private fun loginWithEmail() {
        viewModelScope.launch {
            _uiEvent.trySend(LoginUiEvent.LoginWithEmail)
        }
    }

    private fun navigateToSignUp() {
        viewModelScope.launch {
            _uiEvent.trySend(LoginUiEvent.NavigateToSignUp)
        }
    }
}