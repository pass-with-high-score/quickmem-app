package com.pwhs.quickmem.domain.model.auth

data class AuthSocialGoogleRequestModel(
    val id: String,
    val email: String,
    val provider: String,
    val displayName: String,
    val photoUrl: String,
    val idToken: String,
)