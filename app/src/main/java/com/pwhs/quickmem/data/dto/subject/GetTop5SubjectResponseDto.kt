package com.pwhs.quickmem.data.dto.subject

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetTop5SubjectResponseDto (
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("studySetCount")
    val studySetCount: Int
)