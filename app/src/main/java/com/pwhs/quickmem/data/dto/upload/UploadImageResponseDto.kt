package com.pwhs.quickmem.data.dto.upload

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadImageResponseDto(
    @SerialName("message")
    val message: String,
    @SerialName("url")
    val url: String
)