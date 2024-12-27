package com.pwhs.quickmem.domain.model.users

data class UpdateCoinResponseModel(
    val message: String,
    val coinAction: String,
    val coins: Int,
)