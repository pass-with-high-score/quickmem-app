package com.pwhs.quickmem.data.dto.flashcard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VoiceDto(
    @SerialName("code")
    val code: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("name")
    val name: String,
)
