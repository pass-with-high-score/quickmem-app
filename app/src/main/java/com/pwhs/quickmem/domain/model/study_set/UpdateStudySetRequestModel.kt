package com.pwhs.quickmem.domain.model.study_set

data class UpdateStudySetRequestModel(
    val colorId: Int,
    val description: String,
    val isPublic: Boolean,
    val ownerId: String,
    val subjectId: Int,
    val title: String,
)