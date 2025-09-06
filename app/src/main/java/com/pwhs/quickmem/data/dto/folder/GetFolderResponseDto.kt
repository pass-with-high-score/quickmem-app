package com.pwhs.quickmem.data.dto.folder

import kotlinx.serialization.SerialName
import com.pwhs.quickmem.data.dto.study_set.GetStudySetResponseDto
import com.pwhs.quickmem.data.dto.user.UserResponseDto
import kotlinx.serialization.Serializable

@Serializable
data class GetFolderResponseDto(
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
    @SerialName("owner")
    val owner: UserResponseDto,
    @SerialName("studySets")
    val studySets: List<GetStudySetResponseDto>? = null,
    @SerialName("linkShareCode")
    val linkShareCode: String? = null,
    @SerialName("isImported")
    val isImported: Boolean? = null,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
)
