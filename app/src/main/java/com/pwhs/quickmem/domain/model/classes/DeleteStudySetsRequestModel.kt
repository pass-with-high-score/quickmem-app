package com.pwhs.quickmem.domain.model.classes

data class DeleteStudySetsRequestModel(
    val userId: String,
    val classId: String,
    val studySetId: String,
)