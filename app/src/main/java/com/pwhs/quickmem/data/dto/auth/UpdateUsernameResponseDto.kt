package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUsernameResponseDto(
    @SerialName("newUsername")
    val newUsername: String,
    @SerialName("message")
    val message: String
)