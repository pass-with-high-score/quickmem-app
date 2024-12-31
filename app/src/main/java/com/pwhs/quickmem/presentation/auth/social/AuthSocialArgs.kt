package com.pwhs.quickmem.presentation.auth.social

import kotlinx.serialization.Serializable

@Serializable
data class AuthSocialArgs(
    val id: String,
    val email: String,
    val provider: String,
    val displayName: String,
    val photoUrl: String,
    val idToken: String,
)
