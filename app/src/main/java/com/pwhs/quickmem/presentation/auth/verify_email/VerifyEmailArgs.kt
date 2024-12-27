package com.pwhs.quickmem.presentation.auth.verify_email

import kotlinx.serialization.Serializable

@Serializable
data class VerifyEmailArgs(
    val email: String,
    val isFromSignup: Boolean,
    val resetPasswordToken: String,
)