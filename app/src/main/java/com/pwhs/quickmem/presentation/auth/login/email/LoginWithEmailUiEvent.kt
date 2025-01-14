package com.pwhs.quickmem.presentation.auth.login.email

import androidx.annotation.StringRes


sealed class LoginWithEmailUiEvent {
    data object LoginSuccess : LoginWithEmailUiEvent()
    data class LoginFailure(
        @StringRes val message: Int,
    ) : LoginWithEmailUiEvent()

    data class NavigateToVerifyEmail(
        val email: String,
    ) : LoginWithEmailUiEvent()
}