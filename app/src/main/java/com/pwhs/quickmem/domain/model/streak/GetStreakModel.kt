package com.pwhs.quickmem.domain.model.streak

data class GetStreakModel(
    val userId: String,
    val streaks: List<StreakModel>,
)
