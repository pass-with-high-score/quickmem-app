package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateFullNameResponseDto(
    @SerialName("message")
    val message: String,
    @SerialName("fullname")
    val fullname: String
)