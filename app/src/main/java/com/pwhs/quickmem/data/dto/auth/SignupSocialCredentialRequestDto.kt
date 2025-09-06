package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupSocialCredentialRequestDto(
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("idToken")
    val idToken: String,
    @SerialName("photoUrl")
    val photoUrl: String,
    @SerialName("birthday")
    val birthday: String,
    @SerialName("id")
    val id: String,
    @SerialName("provider")
    val provider: String,
    @SerialName("displayName")
    val displayName: String,
)