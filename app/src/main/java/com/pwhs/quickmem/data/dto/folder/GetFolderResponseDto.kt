package com.pwhs.quickmem.data.dto.folder

import com.google.gson.annotations.SerializedName
import com.pwhs.quickmem.data.dto.study_set.GetStudySetResponseDto
import com.pwhs.quickmem.data.dto.user.UserResponseDto

data class GetFolderResponseDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("isPublic")
    val isPublic: Boolean,
    @SerializedName("studySetCount")
    val studySetCount: Int,
    @SerializedName("owner")
    val owner: UserResponseDto,
    @SerializedName("studySets")
    val studySets: List<GetStudySetResponseDto>? = null,
    @SerializedName("linkShareCode")
    val linkShareCode: String? = null,
    @SerializedName("isImported")
    val isImported: Boolean? = null,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
)
