package com.pwhs.quickmem.domain.model.subject

data class GetTop5SubjectResponseModel(
    val id: Int,
    val name: String,
    val studySetCount: Int,
)