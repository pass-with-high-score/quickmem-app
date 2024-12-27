package com.pwhs.quickmem.domain.model.study_set

data class AddStudySetToClassRequestModel(
    val userId: String,
    val classId: String,
    val studySetIds: List<String>,
)