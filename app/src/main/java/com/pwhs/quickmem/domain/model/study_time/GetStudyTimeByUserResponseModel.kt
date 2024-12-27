package com.pwhs.quickmem.domain.model.study_time

data class GetStudyTimeByUserResponseModel(
    val flip: Int,
    val quiz: Int,
    val userId: String,
    val total: Int,
    val trueFalse: Int,
    val write: Int,
)
