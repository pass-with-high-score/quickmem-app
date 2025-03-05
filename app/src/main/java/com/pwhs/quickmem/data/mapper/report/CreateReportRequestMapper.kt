package com.pwhs.quickmem.data.mapper.report

import com.pwhs.quickmem.data.dto.report.CreateReportRequestDto
import com.pwhs.quickmem.domain.model.report.CreateReportRequestModel

fun CreateReportRequestModel.toDto(): CreateReportRequestDto {
    return CreateReportRequestDto(
        reason = reason,
        reportedEntityId = reportedEntityId,
        ownerOfReportedEntityId = ownerOfReportedEntityId,
        reportedType = reportedType,
    )
}

fun CreateReportRequestDto.toModel(): CreateReportRequestModel {
    return CreateReportRequestModel(
        reason = reason,
        reportedEntityId = reportedEntityId,
        ownerOfReportedEntityId = ownerOfReportedEntityId,
        reportedType = reportedType,
    )
}