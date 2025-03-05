package com.pwhs.quickmem.data.dto.study_set

import com.google.gson.annotations.SerializedName

data class MakeACopyStudySetRequestDto(
    @SerializedName("studySetId")
    val studySetId: String,
)

