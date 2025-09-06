package com.pwhs.quickmem.data.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvatarResponseDto(
    @SerialName("id")
    val id: Int,
    @SerialName("url")
    val url: String
)
