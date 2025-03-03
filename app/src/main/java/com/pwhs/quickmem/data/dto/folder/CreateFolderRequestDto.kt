package com.pwhs.quickmem.data.dto.folder

import com.google.gson.annotations.SerializedName

data class CreateFolderRequestDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("isPublic")
    val isPublic: Boolean,
)
