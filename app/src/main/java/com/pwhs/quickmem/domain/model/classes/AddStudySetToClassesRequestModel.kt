package com.pwhs.quickmem.domain.model.classes

data class AddStudySetToClassesRequestModel(
    val studySetId: String,
    val classIds: List<String>,
)