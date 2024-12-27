package com.pwhs.quickmem.data.remote.repository

import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.data.mapper.pixabay.toModel
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.model.pixabay.SearchImageResponseModel
import com.pwhs.quickmem.domain.repository.PixaBayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class PixaBayRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : PixaBayRepository {
    override suspend fun searchImages(
        token: String,
        query: String,
    ): Flow<Resources<SearchImageResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.searchImage(token = token, query = query)
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.message ?: "An error occurred"))
            }
        }
    }
}