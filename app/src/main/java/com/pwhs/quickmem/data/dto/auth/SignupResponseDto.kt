package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupResponseDto(
    @SerialName("message")
    val message: String,
    @SerialName("isVerified")
    val isVerified: Boolean,
    @SerialName("success")
    val success: Boolean,
)