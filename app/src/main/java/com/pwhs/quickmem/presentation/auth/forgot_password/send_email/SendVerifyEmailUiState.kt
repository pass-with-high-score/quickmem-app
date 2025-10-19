package com.pwhs.quickmem.presentation.auth.forgot_password.send_email

import androidx.annotation.StringRes

data class SendVerifyEmailUiState(
    val email: String = "",
    @param:StringRes val emailError: Int? = null,
    val isLoading: Boolean = false,
    val resetPasswordToken: String = ""
)
