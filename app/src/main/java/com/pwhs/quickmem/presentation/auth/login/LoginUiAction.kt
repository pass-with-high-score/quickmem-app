package com.pwhs.quickmem.presentation.auth.login

import com.pwhs.quickmem.domain.model.auth.AuthSocialGoogleRequestModel

sealed class LoginUiAction {
    data object NavigateToSignUp : LoginUiAction()
    data object LoginWithEmail : LoginUiAction()
    data class LoginWithGoogle(val authSocialGoogleRequestModel: AuthSocialGoogleRequestModel) :
        LoginUiAction()
}