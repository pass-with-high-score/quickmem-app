package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateEmailResponseDto(
    @SerialName("message")
    val message: String,
    @SerialName("email")
    val email: String
)
