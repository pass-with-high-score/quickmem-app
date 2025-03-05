package com.pwhs.quickmem.domain.repository

import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.streak.GetStreakModel
import com.pwhs.quickmem.domain.model.streak.GetTopStreakResponseModel
import com.pwhs.quickmem.domain.model.streak.StreakModel
import kotlinx.coroutines.flow.Flow

interface StreakRepository {
    suspend fun getStreaksByUserId(): Flow<Resources<GetStreakModel>>
    suspend fun updateStreak(): Flow<Resources<StreakModel>>
    suspend fun getTopStreaks(
        limit: Int?,
    ): Flow<Resources<List<GetTopStreakResponseModel>>>
}