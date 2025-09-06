package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class OtpResponseDto {
    @SerialName("otp")
    val otp: String? = null
    @SerialName("email")
    val email: String? = null
}