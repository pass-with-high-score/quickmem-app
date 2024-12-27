package com.pwhs.quickmem.domain.model.users

data class UpdateCoinRequestModel(
    val userId: String,
    val coin: Int,
    val action: String,
)