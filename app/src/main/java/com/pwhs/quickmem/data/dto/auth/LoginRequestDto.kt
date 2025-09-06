package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    @SerialName("email")
    val email: String?,
    @SerialName("password")
    val password: String?,
    @SerialName("provider")
    val provider: String? = null,
    @SerialName("idToken")
    val idToken: String? = null,
)
