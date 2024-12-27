package com.pwhs.quickmem.domain.model.auth

data class VerifyPasswordRequestModel(
    val userId: String,
    val password: String,
)
