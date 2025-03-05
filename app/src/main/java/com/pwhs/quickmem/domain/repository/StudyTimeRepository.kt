package com.pwhs.quickmem.domain.repository

import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.study_time.CreateStudyTimeModel
import com.pwhs.quickmem.domain.model.study_time.GetStudyTimeByStudySetResponseModel
import com.pwhs.quickmem.domain.model.study_time.GetStudyTimeByUserResponseModel
import kotlinx.coroutines.flow.Flow

interface StudyTimeRepository {
    suspend fun getStudyTimeByStudySet(
        studySetId: String,
    ): Flow<Resources<GetStudyTimeByStudySetResponseModel>>

    suspend fun getStudyTimeByUser(): Flow<Resources<GetStudyTimeByUserResponseModel>>

    suspend fun createStudyTime(
        createStudyTimeModel: CreateStudyTimeModel,
    ): Flow<Resources<Unit>>
}