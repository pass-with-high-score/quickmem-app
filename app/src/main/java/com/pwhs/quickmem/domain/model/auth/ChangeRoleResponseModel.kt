package com.pwhs.quickmem.domain.model.auth

data class ChangeRoleResponseModel(
    val message: String,
    val role: String,
    val success: Boolean,
)
