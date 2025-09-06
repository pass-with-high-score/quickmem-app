package com.pwhs.quickmem.data.dto.flashcard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LanguageDto(
    @SerialName("code")
    val code: String,
    @SerialName("name")
    val name: String,
    @SerialName("voiceAvailableCount")
    val voiceAvailableCount: Int,
    @SerialName("flag")
    val flag: String,
    @SerialName("country")
    val country: String,
)
