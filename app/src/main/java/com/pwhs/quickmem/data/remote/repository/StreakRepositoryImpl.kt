package com.pwhs.quickmem.data.remote.repository

import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.data.dto.streak.IncreaseStreakDto
import com.pwhs.quickmem.data.mapper.streak.toModel
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.model.streak.GetStreakModel
import com.pwhs.quickmem.domain.model.streak.GetTopStreakResponseModel
import com.pwhs.quickmem.domain.model.streak.StreakModel
import com.pwhs.quickmem.domain.repository.StreakRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class StreakRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : StreakRepository {
    override suspend fun getStreaksByUserId(
        token: String,
        userId: String,
    ): Flow<Resources<GetStreakModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.getStreaksByUserId(token, userId)
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun updateStreak(
        token: String,
        userId: String,
    ): Flow<Resources<StreakModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.updateStreak(token, IncreaseStreakDto(userId))
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getTopStreaks(
        token: String,
        limit: Int?,
    ): Flow<Resources<List<GetTopStreakResponseModel>>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.getTopStreaks(token, limit)
                emit(Resources.Success(response.map { it.toModel() }))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }
}