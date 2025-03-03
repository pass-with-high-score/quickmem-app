package com.pwhs.quickmem.data.remote.repository

import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.data.mapper.study_time.toDto
import com.pwhs.quickmem.data.mapper.study_time.toModel
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.model.study_time.CreateStudyTimeModel
import com.pwhs.quickmem.domain.model.study_time.GetStudyTimeByStudySetResponseModel
import com.pwhs.quickmem.domain.model.study_time.GetStudyTimeByUserResponseModel
import com.pwhs.quickmem.domain.repository.StudyTimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class StudyTimeRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : StudyTimeRepository {
    override suspend fun getStudyTimeByStudySet(
        studySetId: String,
    ): Flow<Resources<GetStudyTimeByStudySetResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.getStudyTimeByStudySet(studySetId)
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error("Error"))
            }
        }
    }

    override suspend fun getStudyTimeByUser(
        userId: String,
    ): Flow<Resources<GetStudyTimeByUserResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.getStudyTimeByUser(userId)
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error("Error"))
            }
        }
    }

    override suspend fun createStudyTime(
        createStudyTimeModel: CreateStudyTimeModel,
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.createStudyTime(createStudyTimeModel.toDto())
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error("Error"))
            }
        }
    }

}