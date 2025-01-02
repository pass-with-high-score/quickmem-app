package com.pwhs.quickmem.presentation.auth.login

import androidx.annotation.StringRes
import com.pwhs.quickmem.domain.model.auth.AuthSocialGoogleRequestModel

sealed class LoginUiEvent {
    data object NavigateToSignUp : LoginUiEvent()
    data class NavigateToLoginWithEmail(val email: String) : LoginUiEvent()
    data object LoginWithEmail : LoginUiEvent()
    data object LoginWithFacebook : LoginUiEvent()
    data object LoginSuccess : LoginUiEvent()
    data class LoginFailure(val authSocialGoogleRequestModel: AuthSocialGoogleRequestModel) :
        LoginUiEvent()

    data class ShowError(@StringRes val error: Int) : LoginUiEvent()
}
