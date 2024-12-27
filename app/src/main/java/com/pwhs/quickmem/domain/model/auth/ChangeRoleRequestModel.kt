package com.pwhs.quickmem.domain.model.auth

data class ChangeRoleRequestModel(
    val role: String,
    val userId: String,
)
