package com.pwhs.quickmem.data.dto.folder

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateFolderRequestDto(
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("isPublic")
    val isPublic: Boolean,
)
