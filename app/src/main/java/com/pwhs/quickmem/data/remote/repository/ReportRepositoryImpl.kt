package com.pwhs.quickmem.data.remote.repository

import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.data.mapper.report.toDto
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.model.report.CreateReportRequestModel
import com.pwhs.quickmem.domain.repository.ReportRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : ReportRepository {
    override suspend fun createReport(
        token: String,
        createReportRequestModel: CreateReportRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.createReport(
                    token = token,
                    createReportRequestDto = createReportRequestModel.toDto()
                )
                emit(Resources.Success(response))
            } catch (e: Exception) {
                emit(Resources.Error(e.message ?: "An error occurred"))
            }
        }
    }
}