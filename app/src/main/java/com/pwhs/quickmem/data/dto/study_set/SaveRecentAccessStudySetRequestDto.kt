package com.pwhs.quickmem.data.dto.study_set

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveRecentAccessStudySetRequestDto (
    @SerialName("studySetId")
    val studySetId: String
)