package com.pwhs.quickmem.data.dto.study_set

import com.pwhs.quickmem.data.dto.color.ColorResponseDto
import com.pwhs.quickmem.data.dto.subject.SubjectResponseDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateStudySetResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("color")
    val color: ColorResponseDto,
    @SerialName("description")
    val description: String,
    @SerialName("isPublic")
    val isPublic: Boolean,
    @SerialName("subject")
    val subject: SubjectResponseDto,
    @SerialName("title")
    val title: String,
    @SerialName("isAIGenerated")
    val isAIGenerated: Boolean,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String
)