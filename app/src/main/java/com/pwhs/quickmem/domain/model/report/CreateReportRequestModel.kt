package com.pwhs.quickmem.domain.model.report

data class CreateReportRequestModel(
    val reason: String,
    val reportedEntityId: String,
    val ownerOfReportedEntityId: String,
    val reportedType: String,
)
