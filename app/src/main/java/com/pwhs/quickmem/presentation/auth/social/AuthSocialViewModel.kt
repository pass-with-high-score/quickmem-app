package com.pwhs.quickmem.presentation.auth.social

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.AuthProvider
import com.pwhs.quickmem.core.data.enums.UserStatus
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.auth.SignupSocialCredentialRequestModel
import com.pwhs.quickmem.domain.repository.AuthRepository
import com.pwhs.quickmem.utils.getUsernameFromEmail
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
class AuthSocialViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val appManager: AppManager,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthSocialUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<AuthSocialUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val email = savedStateHandle.get<String>("email") ?: ""
        val fullName = savedStateHandle.get<String>("displayName") ?: ""
        val avatarUrl = savedStateHandle.get<String>("photoUrl") ?: ""
        val token = savedStateHandle.get<String>("idToken") ?: ""
        val provider = savedStateHandle.get<String>("provider") ?: ""
        val id = savedStateHandle.get<String>("id") ?: ""

        _uiState.value = AuthSocialUiState(
            email = email,
            fullName = fullName,
            avatarUrl = avatarUrl,
            token = token,
            provider = AuthProvider.valueOf(provider),
            id = id,
        )
    }

    fun onEvent(event: AuthSocialUiAction) {
        when (event) {
            is AuthSocialUiAction.OnAvatarUrlChanged -> {
                _uiState.value = _uiState.value.copy(avatarUrl = event.avatarUrl)
            }

            is AuthSocialUiAction.OnBirthDayChanged -> {
                _uiState.value = _uiState.value.copy(birthDay = event.birthDay)
            }

            is AuthSocialUiAction.OnEmailChanged -> {
                _uiState.value = _uiState.value.copy(email = event.email)
            }

            is AuthSocialUiAction.OnNameChanged -> {
                _uiState.value = _uiState.value.copy(fullName = event.name)
            }

            is AuthSocialUiAction.OnRoleChanged -> {
                _uiState.value = _uiState.value.copy(role = event.role)
            }

            is AuthSocialUiAction.Register -> {
                if (validateInput()) {
                    authSocial()
                } else {
                    _uiEvent.trySend(AuthSocialUiEvent.SignUpFailure(R.string.txt_invalid_input))
                }
            }
        }
    }

    private fun authSocial() {
        val username = uiState.value.email.getUsernameFromEmail()
        val idToken = uiState.value.token
        val email = uiState.value.email
        val photoUrl = uiState.value.avatarUrl
        val displayName = uiState.value.fullName
        val role = uiState.value.role
        val birthday = uiState.value.birthDay
        val id = uiState.value.id
        val provider = uiState.value.provider?.name ?: ""

        val request = SignupSocialCredentialRequestModel(
            username = username,
            email = email,
            idToken = idToken,
            photoUrl = photoUrl,
            role = role.name,
            birthday = birthday,
            id = id,
            provider = provider,
            displayName = displayName,
        )

        viewModelScope.launch {
            authRepository.signupWithGoogle(request).collect { resource ->
                when (resource) {
                    is Resources.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEvent.trySend(AuthSocialUiEvent.SignUpFailure(R.string.txt_error_occurred))
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
                            _uiEvent.trySend(AuthSocialUiEvent.SignUpFailure(R.string.txt_your_account_has_been_blocked))
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
                            appManager.saveUserRole(resource.data?.role ?: "")
                            appManager.saveUserCoins(resource.data?.coin ?: 0)
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
                            _uiEvent.trySend(AuthSocialUiEvent.SignUpSuccess)
                        }
                    }
                }

            }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true

        if (uiState.value.birthDay.isEmpty()) {
            _uiState.update { it.copy(birthdayError = R.string.txt_birthday_is_required) }
            isValid = false
        } else {
            _uiState.update { it.copy(birthdayError = null) }
        }

        return isValid
    }
}