package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateAvatarResponseDto(
    @SerialName("message")
    val message:String,
    @SerialName("avatarUrl")
    val avatarUrl:String
)