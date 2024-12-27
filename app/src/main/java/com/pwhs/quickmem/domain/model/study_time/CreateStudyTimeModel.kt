package com.pwhs.quickmem.domain.model.study_time


data class CreateStudyTimeModel(
    val learnMode: String,
    val studySetId: String,
    val timeSpent: Int,
    val userId: String,
)