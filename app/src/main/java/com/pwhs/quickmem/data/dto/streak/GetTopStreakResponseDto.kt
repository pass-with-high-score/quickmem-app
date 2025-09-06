package com.pwhs.quickmem.data.dto.streak

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetTopStreakResponseDto (
    @SerialName("userId")
    val userId: String,
    @SerialName("username")
    val username: String,
    @SerialName("avatarUrl")
    val avatarUrl: String,
    @SerialName("streakCount")
    val streakCount: Int,
)