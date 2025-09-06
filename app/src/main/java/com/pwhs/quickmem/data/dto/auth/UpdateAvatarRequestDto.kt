package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateAvatarRequestDto(
    @SerialName("avatar")
    val avatar:String
)