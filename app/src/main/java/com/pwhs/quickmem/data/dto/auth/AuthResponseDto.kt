package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("fullName")
    val fullName: String? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("username")
    val username: String? = null,
    @SerialName("avatarUrl")
    val avatarUrl: String? = null,
    @SerialName("birthday")
    val birthday: String? = null,
    @SerialName("accessToken")
    val accessToken: String? = null,
    @SerialName("refreshToken")
    val refreshToken: String? = null,
    @SerialName("provider")
    val provider: List<String>? = null,
    @SerialName("isVerified")
    val isVerified: Boolean? = null,
    @SerialName("coin")
    val coin: Int? = null,
    @SerialName("bannedAt")
    val bannedAt: String? = null,
    @SerialName("userStatus")
    val userStatus: String? = null,
    @SerialName("bannedReason")
    val bannedReason: String? = null,
)