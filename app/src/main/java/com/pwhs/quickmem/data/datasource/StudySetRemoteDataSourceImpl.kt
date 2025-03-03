package com.pwhs.quickmem.data.datasource

import com.pwhs.quickmem.data.mapper.study_set.toModel
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.datasource.StudySetRemoteDataSource
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.presentation.app.search_result.study_set.enums.SearchResultCreatorEnum
import com.pwhs.quickmem.presentation.app.search_result.study_set.enums.SearchResultSizeEnum
import timber.log.Timber

class StudySetRemoteDataSourceImpl(
    private val apiService: ApiService
) : StudySetRemoteDataSource {
    override suspend fun getSearchResultStudySets(
        title: String,
        size: SearchResultSizeEnum,
        creatorType: SearchResultCreatorEnum?,
        page: Int,
        colorId: Int?,
        subjectId: Int?,
        isAIGenerated: Boolean?
    ): List<GetStudySetResponseModel> {
        try {
            val response =
                apiService.searchStudySet(
                    title = title,
                    size = size.query,
                    creatorType = creatorType?.query,
                    page = page,
                    colorId = colorId,
                    subjectId = subjectId,
                    isAIGenerated = isAIGenerated
                )
            return response.map { it.toModel() }
        } catch (e: Exception) {
            Timber.e(e.toString())
            throw e
        }
    }
}