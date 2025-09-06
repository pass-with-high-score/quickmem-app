package com.pwhs.quickmem.data.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("avatarUrl")
    val avatarUrl: String,
    @SerialName("username")
    val username: String
)