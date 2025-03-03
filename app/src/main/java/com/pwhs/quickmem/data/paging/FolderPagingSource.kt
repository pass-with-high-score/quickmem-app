package com.pwhs.quickmem.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pwhs.quickmem.domain.datasource.FolderRemoteDataSource
import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel

class FolderPagingSource(
    private val folderRemoteDataSource: FolderRemoteDataSource,
    private val title: String,
) : PagingSource<Int, GetFolderResponseModel>() {
    override fun getRefreshKey(state: PagingState<Int, GetFolderResponseModel>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetFolderResponseModel> {
        return try {
            val currentPage = params.key ?: 1
            val response = folderRemoteDataSource.getSearchResultFolders(
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