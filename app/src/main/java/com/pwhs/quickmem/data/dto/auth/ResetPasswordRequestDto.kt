package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequestDto(
    @SerialName("email")
    val email: String,
    @SerialName("newPassword")
    val newPassword: String,
    @SerialName("otp")
    val otp: String,
    @SerialName("resetPasswordToken")
    val resetPasswordToken: String
)