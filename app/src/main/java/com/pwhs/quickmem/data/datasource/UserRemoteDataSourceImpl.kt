package com.pwhs.quickmem.data.datasource

import com.pwhs.quickmem.data.mapper.user.toModel
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.datasource.UserRemoteDataResource
import com.pwhs.quickmem.domain.model.users.SearchUserResponseModel
import timber.log.Timber

class UserRemoteDataSourceImpl(
    private val apiService: ApiService
) : UserRemoteDataResource {
    override suspend fun searchUser(
        username: String,
        page: Int?
    ): List<SearchUserResponseModel> {
        try {
            val response = apiService.searchUser(username = username, page = page)
            return response.map { it.toModel() }
        } catch (e: Exception) {
            Timber.e(e.toString())
            throw e
        }
    }
}