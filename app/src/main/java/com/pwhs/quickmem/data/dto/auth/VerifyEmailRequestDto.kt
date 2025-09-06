package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyEmailRequestDto (
    @SerialName("otp")
    val otp: String?,
    @SerialName("email")
    val email: String?,
)