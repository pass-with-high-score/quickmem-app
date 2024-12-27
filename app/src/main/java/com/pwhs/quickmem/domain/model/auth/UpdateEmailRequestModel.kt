package com.pwhs.quickmem.domain.model.auth

data class UpdateEmailRequestModel(
    val userId: String,
    val email: String,
)