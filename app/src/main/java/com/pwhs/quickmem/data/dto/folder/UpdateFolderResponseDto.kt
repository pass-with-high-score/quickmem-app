package com.pwhs.quickmem.data.dto.folder

import kotlinx.serialization.SerialName
import com.pwhs.quickmem.data.dto.study_set.GetStudySetResponseDto
import kotlinx.serialization.Serializable

@Serializable
data class UpdateFolderResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("isPublic")
    val isPublic: Boolean,
    @SerialName("studySetCount")
    val studySetCount: Int,
    @SerialName("studySets")
    val studySets: List<GetStudySetResponseDto>,
    @SerialName("linkShareCode")
    val linkShareCode: String? = null,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("createdAt")
    val createdAt: String
)