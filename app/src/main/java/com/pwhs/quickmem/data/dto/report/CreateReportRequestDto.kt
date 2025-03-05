package com.pwhs.quickmem.data.dto.report

import com.google.gson.annotations.SerializedName

data class CreateReportRequestDto(
    @SerializedName("reason")
    val reason: String,
    @SerializedName("reportedEntityId")
    val reportedEntityId: String,
    @SerializedName("ownerOfReportedEntityId")
    val ownerOfReportedEntityId: String,
    @SerializedName("reportedType")
    val reportedType: String,
)
