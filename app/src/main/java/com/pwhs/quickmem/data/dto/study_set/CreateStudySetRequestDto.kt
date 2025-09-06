package com.pwhs.quickmem.data.dto.study_set

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateStudySetRequestDto(
    @SerialName("colorId")
    val colorId: Int,
    @SerialName("description")
    val description: String,
    @SerialName("isPublic")
    val isPublic: Boolean,
    @SerialName("subjectId")
    val subjectId: Int,
    @SerialName("title")
    val title: String
)