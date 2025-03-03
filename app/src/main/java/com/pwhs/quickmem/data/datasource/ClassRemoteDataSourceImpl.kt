package com.pwhs.quickmem.data.datasource

import com.pwhs.quickmem.data.mapper.classes.toModel
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.datasource.ClassRemoteDataSource
import com.pwhs.quickmem.domain.model.classes.GetClassByOwnerResponseModel
import timber.log.Timber

class ClassRemoteDataSourceImpl(
    private val apiService: ApiService
) : ClassRemoteDataSource {
    override suspend fun getSearchResultClasses(
        title: String,
        page: Int?
    ) : List<GetClassByOwnerResponseModel> {
        try {
            val response = apiService.searchClass(title, page)
            return response.map { it.toModel() }
        } catch (e: Exception) {
            Timber.e(e.toString())
            throw e
        }
    }
}