package com.pwhs.quickmem.domain.model.auth

data class UpdateUsernameRequestModel(
    val userId: String,
    val newUsername: String,
)