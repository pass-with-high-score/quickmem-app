package com.pwhs.quickmem.domain.model.auth

data class ChangePasswordRequestModel(
    val oldPassword: String,
    val newPassword: String,
)