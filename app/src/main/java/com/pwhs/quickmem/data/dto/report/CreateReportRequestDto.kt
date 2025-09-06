package com.pwhs.quickmem.data.dto.report

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateReportRequestDto(
    @SerialName("reason")
    val reason: String,
    @SerialName("reportedEntityId")
    val reportedEntityId: String,
    @SerialName("ownerOfReportedEntityId")
    val ownerOfReportedEntityId: String,
    @SerialName("reportedType")
    val reportedType: String,
)
