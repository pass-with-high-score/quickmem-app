package com.pwhs.quickmem.presentation.app.report

data class ReportUiState(
    val isLoading: Boolean = false,
    val reportType: ReportTypeEnum? = null,
    val reportedEntityId: String = "",
    val reason: String = "",
    val ownerOfReportedEntity: String = "",
)