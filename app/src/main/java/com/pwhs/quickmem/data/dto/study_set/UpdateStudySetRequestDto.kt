package com.pwhs.quickmem.data.dto.study_set

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateStudySetRequestDto(
    @SerialName("colorId")
    val colorId: Int,
    @SerialName("description")
    val description: String,
    @SerialName("isPublic")
    val isPublic: Boolean,
    @SerialName("ownerId")
    val ownerId: String,
    @SerialName("subjectId")
    val subjectId: Int,
    @SerialName("title")
    val title: String
)