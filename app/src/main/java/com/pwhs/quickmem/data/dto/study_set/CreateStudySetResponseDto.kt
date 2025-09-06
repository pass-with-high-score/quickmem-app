package com.pwhs.quickmem.data.dto.study_set

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateStudySetResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("colorId")
    val colorId: Long,
    @SerialName("description")
    val description: String,
    @SerialName("isPublic")
    val isPublic: Boolean,
    @SerialName("subjectId")
    val subjectId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("isAIGenerated")
    val isAIGenerated: Boolean,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String
)