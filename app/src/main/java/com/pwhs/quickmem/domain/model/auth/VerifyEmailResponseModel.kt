package com.pwhs.quickmem.domain.model.auth

data class VerifyEmailResponseModel(
    val otp: String? = null,
    val email: String? = null,
)
