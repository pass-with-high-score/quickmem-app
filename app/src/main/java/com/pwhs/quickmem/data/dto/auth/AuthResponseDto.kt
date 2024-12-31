package com.pwhs.quickmem.data.dto.auth

import com.google.gson.annotations.SerializedName
import com.pwhs.quickmem.core.data.enums.UserStatus

data class AuthResponseDto(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("fullName")
    val fullName: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("role")
    val role: String? = null,
    @SerializedName("avatarUrl")
    val avatarUrl: String? = null,
    @SerializedName("birthday")
    val birthday: String? = null,
    @SerializedName("accessToken")
    val accessToken: String? = null,
    @SerializedName("refreshToken")
    val refreshToken: String? = null,
    @SerializedName("provider")
    val provider: List<String>? = null,
    @SerializedName("isVerified")
    val isVerified: Boolean? = null,
    @SerializedName("coin")
    val coin: Int? = null,
    @SerializedName("bannedAt")
    val bannedAt: String? = null,
    @SerializedName("userStatus")
    val userStatus: String? = null,
    @SerializedName("bannedReason")
    val bannedReason: String? = null,
)