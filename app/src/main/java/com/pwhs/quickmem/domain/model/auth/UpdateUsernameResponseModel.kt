package com.pwhs.quickmem.domain.model.auth

data class UpdateUsernameResponseModel(
    val newUsername: String,
    val message: String,
)