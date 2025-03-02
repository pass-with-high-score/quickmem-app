package com.pwhs.quickmem.domain.model.study_set

data class AddStudySetToClassRequestModel(
    val classId: String,
    val studySetIds: List<String>,
)