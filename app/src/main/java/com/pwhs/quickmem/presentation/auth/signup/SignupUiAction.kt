package com.pwhs.quickmem.presentation.auth.signup

import com.pwhs.quickmem.domain.model.auth.AuthSocialGoogleRequestModel

sealed class SignupUiAction {
    data class SignupWithGoogle(val authSocialGoogleRequestModel: AuthSocialGoogleRequestModel) :
        SignupUiAction()
}