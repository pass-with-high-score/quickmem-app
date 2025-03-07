package com.pwhs.quickmem.data.dto.study_set

import com.google.gson.annotations.SerializedName

data class CreateStudySetResponseDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("colorId")
    val colorId: Long,
    @SerializedName("description")
    val description: String,
    @SerializedName("isPublic")
    val isPublic: Boolean,
    @SerializedName("subjectId")
    val subjectId: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("isAIGenerated")
    val isAIGenerated: Boolean,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)