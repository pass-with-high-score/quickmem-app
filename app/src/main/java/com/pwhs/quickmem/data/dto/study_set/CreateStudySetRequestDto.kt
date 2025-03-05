package com.pwhs.quickmem.data.dto.study_set

import com.google.gson.annotations.SerializedName

data class CreateStudySetRequestDto(
    @SerializedName("colorId")
    val colorId: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("isPublic")
    val isPublic: Boolean,
    @SerializedName("subjectId")
    val subjectId: Int,
    @SerializedName("title")
    val title: String
)