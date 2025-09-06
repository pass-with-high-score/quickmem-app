package com.pwhs.quickmem.data.dto.study_time

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateStudyTimeDto(
    @SerialName("learnMode")
    val learnMode: String,
    @SerialName("studySetId")
    val studySetId: String,
    @SerialName("timeSpent")
    val timeSpent: Int,
)