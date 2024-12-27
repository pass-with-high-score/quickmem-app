package com.pwhs.quickmem.domain.model.auth

data class ResetPasswordRequestModel(
    val email: String,
    val newPassword: String,
    val otp: String,
    val resetPasswordToken: String,
)