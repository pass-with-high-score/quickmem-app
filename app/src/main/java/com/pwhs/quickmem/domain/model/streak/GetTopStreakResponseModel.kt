package com.pwhs.quickmem.domain.model.streak

data class GetTopStreakResponseModel(
    val userId: String,
    val username: String,
    val avatarUrl: String,
    val streakCount: Int,
)
