package com.pwhs.quickmem.domain.model.auth

data class SendResetPasswordResponseModel(
    val email: String,
    val isSent: Boolean,
    val message: String,
    val resetPasswordToken: String,
)