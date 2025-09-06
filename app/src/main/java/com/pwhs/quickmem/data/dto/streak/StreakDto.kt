package com.pwhs.quickmem.data.dto.streak

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreakDto(
    @SerialName("id")
    val id: String,
    @SerialName("streakCount")
    val streakCount: Int,
    @SerialName("date")
    val date: String,
)
