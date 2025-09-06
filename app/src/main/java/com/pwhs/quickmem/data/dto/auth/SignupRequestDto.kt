package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupRequestDto(
    @SerialName("avatarUrl")
    val avatarUrl: String?,
    @SerialName("email")
    val email: String?,
    @SerialName("username")
    val username: String?,
    @SerialName("fullName")
    val fullName: String?,
    @SerialName("birthday")
    val birthday: String?,
    @SerialName("password")
    val password: String?,
    @SerialName("provider")
    val provider: String? = null,
)