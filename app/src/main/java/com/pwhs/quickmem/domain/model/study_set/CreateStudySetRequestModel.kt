package com.pwhs.quickmem.domain.model.study_set

data class CreateStudySetRequestModel(
    val colorId: Int,
    val description: String,
    val isPublic: Boolean,
    val subjectId: Int,
    val title: String,
)