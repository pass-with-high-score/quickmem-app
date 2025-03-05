package com.pwhs.quickmem.domain.model.auth


data class SignupSocialCredentialRequestModel(
    val username: String,
    val email: String,
    val idToken: String,
    val photoUrl: String,
    val birthday: String,
    val id: String,
    val provider: String,
    val displayName: String,
)