package com.pwhs.quickmem.data.dto.streak

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetStreakDto(
    @SerialName("userId")
    val userId: String,
    @SerialName("streaks")
    val streaks: List<StreakDto>
)
