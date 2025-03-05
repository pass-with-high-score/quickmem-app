package com.pwhs.quickmem.domain.model.auth

data class SignupRequestModel(
    val avatarUrl: String?,
    val email: String?,
    val username: String?,
    val fullName: String?,
    val birthday: String?,
    val password: String? = null,
    val authProvider: String?,
    val facebookId: String? = null,
)