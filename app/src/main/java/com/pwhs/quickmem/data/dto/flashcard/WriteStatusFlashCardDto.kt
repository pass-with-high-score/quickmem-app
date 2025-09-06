package com.pwhs.quickmem.data.dto.flashcard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WriteStatusFlashCardDto(
    @SerialName("writeStatus")
    val writeStatus: String,
)