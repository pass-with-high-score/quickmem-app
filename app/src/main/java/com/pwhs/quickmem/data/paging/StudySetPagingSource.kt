package com.pwhs.quickmem.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pwhs.quickmem.domain.datasource.StudySetRemoteDataSource
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.presentation.app.search_result.study_set.enums.SearchResultSizeEnum
import retrofit2.HttpException
import java.io.IOException

class StudySetPagingSource(
    private val studySetRemoteDataSource: StudySetRemoteDataSource,
    private val title: String,
    private val size: SearchResultSizeEnum,
    private val colorId: Int?,
    private val subjectId: Int?,
    private val isAIGenerated: Boolean?
) : PagingSource<Int, GetStudySetResponseModel>() {
    override fun getRefreshKey(state: PagingState<Int, GetStudySetResponseModel>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetStudySetResponseModel> {
        return try {
            val currentPage = params.key ?: 1
            val response = studySetRemoteDataSource.getSearchResultStudySets(
                title = title,
                size = size,
                page = currentPage,
                colorId = colorId,
                subjectId = subjectId,
                isAIGenerated = isAIGenerated
            )
            LoadResult.Page(
                data = response,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (response.isEmpty()) null else currentPage + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}