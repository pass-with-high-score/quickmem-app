package com.pwhs.quickmem.domain.datasource

import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.presentation.app.search_result.study_set.enums.SearchResultSizeEnum

interface StudySetRemoteDataSource {
    suspend fun getSearchResultStudySets(
        title: String,
        size: SearchResultSizeEnum,
        page: Int,
        colorId: Int?,
        subjectId: Int?,
        isAIGenerated: Boolean?,
    ): List<GetStudySetResponseModel>
}