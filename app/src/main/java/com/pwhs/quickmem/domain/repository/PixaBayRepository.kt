package com.pwhs.quickmem.domain.repository

import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.pixabay.SearchImageResponseModel
import kotlinx.coroutines.flow.Flow

interface PixaBayRepository {
    suspend fun searchImages(
        query: String,
    ): Flow<Resources<SearchImageResponseModel>>
}