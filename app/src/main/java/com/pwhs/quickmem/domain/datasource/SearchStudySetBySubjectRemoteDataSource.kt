package com.pwhs.quickmem.domain.datasource

import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel

interface SearchStudySetBySubjectRemoteDataSource {
    suspend fun getStudySetBySubjectId(
        subjectId: Int,
        page: Int,
    ): List<GetStudySetResponseModel>
}