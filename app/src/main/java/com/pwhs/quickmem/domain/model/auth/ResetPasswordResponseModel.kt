package com.pwhs.quickmem.domain.model.auth

data class ResetPasswordResponseModel(
    val email: String,
    val isReset: Boolean,
    val message: String,
)