package com.pwhs.quickmem.domain.model.auth

data class UpdateFullNameRequestModel(
    val userId: String,
    val fullname: String,
)