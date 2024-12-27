package com.pwhs.quickmem.domain.model.auth

data class ChangePasswordResponseModel(
    val isSet: Boolean,
    val message: String,
    val email: String,
)