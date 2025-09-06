package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordResponseDto(
    @SerialName("email")
    val email: String,
    @SerialName("isReset")
    val isReset: Boolean,
    @SerialName("message")
    val message: String
)