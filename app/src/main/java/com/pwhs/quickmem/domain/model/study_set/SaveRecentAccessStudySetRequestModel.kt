package com.pwhs.quickmem.domain.model.study_set

data class SaveRecentAccessStudySetRequestModel(
    val userId: String,
    val studySetId: String,
)