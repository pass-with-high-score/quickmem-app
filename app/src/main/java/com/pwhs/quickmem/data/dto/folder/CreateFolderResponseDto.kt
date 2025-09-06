package com.pwhs.quickmem.data.dto.folder

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateFolderResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("isPublic")
    val isPublic: Boolean,
    @SerialName("linkShareCode")
    val linkShareCode: String? = null,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String
)
