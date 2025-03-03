package com.pwhs.quickmem.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pwhs.quickmem.domain.datasource.UserRemoteDataResource
import com.pwhs.quickmem.domain.model.users.SearchUserResponseModel

class UserPagingSource(
    private val userRemoteDataResource: UserRemoteDataResource,
    private val username: String,
) : PagingSource<Int, SearchUserResponseModel>() {
    override fun getRefreshKey(state: PagingState<Int, SearchUserResponseModel>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchUserResponseModel> {
        return try {
            val currentPage = params.key ?: 1
            val response = userRemoteDataResource.searchUser(
                username = username,
                page = currentPage
            )
            LoadResult.Page(
                data = response,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (response.isEmpty()) null else currentPage + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

}