package com.pwhs.quickmem.data.dto.study_time

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetStudyTimeByUserResponseDto(
    @SerialName("flip")
    val flip: Int,
    @SerialName("quiz")
    val quiz: Int,
    @SerialName("userId")
    val userId: String,
    @SerialName("total")
    val total: Int,
    @SerialName("trueFalse")
    val trueFalse: Int,
    @SerialName("write")
    val write: Int
)
