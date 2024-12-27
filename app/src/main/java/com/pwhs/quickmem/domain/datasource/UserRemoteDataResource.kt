package com.pwhs.quickmem.domain.datasource

import com.pwhs.quickmem.domain.model.users.SearchUserResponseModel

interface UserRemoteDataResource {
    suspend fun searchUser(
        token: String,
        username: String,
        page: Int?,
    ): List<SearchUserResponseModel>
}