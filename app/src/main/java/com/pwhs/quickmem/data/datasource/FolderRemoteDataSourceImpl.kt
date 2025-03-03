package com.pwhs.quickmem.data.datasource

import com.pwhs.quickmem.data.mapper.folder.toModel
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.datasource.FolderRemoteDataSource
import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import timber.log.Timber

class FolderRemoteDataSourceImpl(
    private val apiService: ApiService
) : FolderRemoteDataSource {
    override suspend fun getSearchResultFolders(
        title: String,
        page: Int?
    ): List<GetFolderResponseModel> {
        try {
            val response = apiService.searchFolder(title = title, page = page)
            return response.map { it.toModel() }
        } catch (e: Exception) {
            Timber.e(e.toString())
            throw e
        }
    }
}