package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthSocialGoogleRequestDto(
    @SerialName("id")
    val id: String,
    @SerialName("email")
    val email: String,
    @SerialName("provider")
    val provider: String,
    @SerialName("displayName")
    val displayName: String,
    @SerialName("photoUrl")
    val photoUrl: String,
    @SerialName("idToken")
    val idToken: String,
)