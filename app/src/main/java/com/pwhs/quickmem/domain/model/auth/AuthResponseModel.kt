package com.pwhs.quickmem.domain.model.auth

data class AuthResponseModel(
    val id: String? = null,
    val fullName: String? = null,
    val email: String? = null,
    val username: String? = null,
    val role: String? = null,
    val avatarUrl: String? = null,
    val birthday: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val provider: List<String>? = null,
    val isVerified: Boolean? = null,
    val coin: Int? = null,
    val bannedAt: String? = null,
    val userStatus: String? = null,
    val bannedReason: String? = null,
)