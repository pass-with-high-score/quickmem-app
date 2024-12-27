package com.pwhs.quickmem.domain.model.study_time

data class GetStudyTimeByStudySetResponseModel(
    val flip: Int,
    val quiz: Int,
    val studySetId: String,
    val total: Int,
    val trueFalse: Int,
    val write: Int,
)
