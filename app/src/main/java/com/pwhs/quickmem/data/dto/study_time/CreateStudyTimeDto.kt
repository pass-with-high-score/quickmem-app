package com.pwhs.quickmem.data.dto.study_time

import com.google.gson.annotations.SerializedName

data class CreateStudyTimeDto(
    @SerializedName("learnMode")
    val learnMode: String,
    @SerializedName("studySetId")
    val studySetId: String,
    @SerializedName("timeSpent")
    val timeSpent: Int,
)