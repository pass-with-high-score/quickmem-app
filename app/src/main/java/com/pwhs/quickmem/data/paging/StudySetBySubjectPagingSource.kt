package com.pwhs.quickmem.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pwhs.quickmem.domain.datasource.SearchStudySetBySubjectRemoteDataSource
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import retrofit2.HttpException
import java.io.IOException

class StudySetBySubjectPagingSource(
    private val searchStudySetBySubjectRemoteDataSource: SearchStudySetBySubjectRemoteDataSource,
    private val subjectId: Int
) : PagingSource<Int, GetStudySetResponseModel>() {
    override fun getRefreshKey(state: PagingState<Int, GetStudySetResponseModel>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetStudySetResponseModel> {
        return try {
            val currentPage = params.key ?: 1
            val response = searchStudySetBySubjectRemoteDataSource.getStudySetBySubjectId(
                subjectId = subjectId,
                page = currentPage
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