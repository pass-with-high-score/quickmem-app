package com.pwhs.quickmem.presentation.auth.signup

import com.pwhs.quickmem.domain.model.auth.AuthSocialGoogleRequestModel

sealed class SignupUiEvent {
    data class SignupWithGoogle(val authSocialGoogleRequestModel: AuthSocialGoogleRequestModel) :
        SignupUiEvent()

    data class ShowError(val error: Int) : SignupUiEvent()
    data object SignupSuccess : SignupUiEvent()
    data class NavigateToLogin(val authSocialGoogleRequestModel: AuthSocialGoogleRequestModel) :
        SignupUiEvent()
}