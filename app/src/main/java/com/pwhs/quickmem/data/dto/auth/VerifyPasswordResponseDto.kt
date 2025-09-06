package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyPasswordResponseDto(
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String
)