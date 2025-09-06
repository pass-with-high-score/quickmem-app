package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyPasswordRequestDto(
    @SerialName("password")
    val password: String
)
