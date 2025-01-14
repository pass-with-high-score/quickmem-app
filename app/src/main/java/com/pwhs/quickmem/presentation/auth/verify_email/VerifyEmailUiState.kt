package com.pwhs.quickmem.presentation.auth.verify_email

data class VerifyEmailUiState(
    val isLoading: Boolean = false,
    val isResetPassword: Boolean = false,
    val otp: String = "",
    val email: String = "",
    val resetPasswordToken: String = "",
    val isOtpValid: Boolean = false,
    val countdown: Int = 0,
)