package com.pwhs.quickmem.domain.repository

import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.report.CreateReportRequestModel
import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    suspend fun createReport(
        createReportRequestModel: CreateReportRequestModel,
    ): Flow<Resources<Unit>>
}