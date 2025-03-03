package com.pwhs.quickmem.data.datasource

import com.pwhs.quickmem.data.mapper.study_set.toModel
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.datasource.SearchStudySetBySubjectRemoteDataSource
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import timber.log.Timber

class SearchStudySetBySubjectDataSourceImpl(
    private val apiService: ApiService
) : SearchStudySetBySubjectRemoteDataSource {
    override suspend fun getStudySetBySubjectId(
        subjectId: Int,
        page: Int
    ): List<GetStudySetResponseModel> {
        try {
            val response = apiService.getStudySetBySubjectId(subjectId = subjectId, page = page)
            return response.map { it.toModel() }
        } catch (e: Exception) {
            Timber.e(e.toString())
            throw e
        }
    }
}