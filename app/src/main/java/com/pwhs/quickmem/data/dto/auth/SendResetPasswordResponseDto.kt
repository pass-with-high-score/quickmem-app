package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendResetPasswordResponseDto(
    @SerialName("email")
    val email: String,
    @SerialName("isSent")
    val isSent: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("resetPasswordToken")
    val resetPasswordToken: String
)