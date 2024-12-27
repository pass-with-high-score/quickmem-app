package com.pwhs.quickmem.domain.model.auth

data class OtpResponseModel(
    val otp: String? = null,
    val email: String? = null,
)