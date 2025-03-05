package com.pwhs.quickmem.data.dto.streak

import com.google.gson.annotations.SerializedName

data class GetTopStreakResponseDto (
    @SerializedName("userId")
    val userId: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("avatarUrl")
    val avatarUrl: String,
    @SerializedName("streakCount")
    val streakCount: Int,
)