package com.pwhs.quickmem.data.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateCoinRequestDto(
    @SerialName("coin")
    val coin: Int,
    @SerialName("action")
    val action: String
)