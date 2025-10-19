package com.pwhs.quickmem.presentation.auth.social

import androidx.annotation.StringRes

sealed class AuthSocialUiEvent {
    data object SignUpSuccess : AuthSocialUiEvent()
    data class SignUpFailure(@param:StringRes val message: Int) : AuthSocialUiEvent()
}