package com.pwhs.quickmem.data.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateCoinResponseDto(
    @SerialName("message")
    val message: String,
    @SerialName("coinAction")
    val coinAction: String,
    @SerialName("coins")
    val coins: Int
)