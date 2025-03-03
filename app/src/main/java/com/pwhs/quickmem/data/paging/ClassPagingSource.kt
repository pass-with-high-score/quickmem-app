package com.pwhs.quickmem.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pwhs.quickmem.domain.datasource.ClassRemoteDataSource
import com.pwhs.quickmem.domain.model.classes.GetClassByOwnerResponseModel

class ClassPagingSource(
    private val classRemoteDataSource: ClassRemoteDataSource,
    private val title: String,
) : PagingSource<Int, GetClassByOwnerResponseModel>() {
    override fun getRefreshKey(state: PagingState<Int, GetClassByOwnerResponseModel>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetClassByOwnerResponseModel> {
        return try {
            val currentPage = params.key ?: 1
            val response = classRemoteDataSource.getSearchResultClasses(
                title = title,
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