package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ChangePasswordResponseDto(
    @SerialName("isSet")
    val isSet: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("email")
    val email: String
)