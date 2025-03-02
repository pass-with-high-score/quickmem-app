package com.pwhs.quickmem.domain.model.users

data class UpdateCoinRequestModel(
    val coin: Int,
    val action: String,
)