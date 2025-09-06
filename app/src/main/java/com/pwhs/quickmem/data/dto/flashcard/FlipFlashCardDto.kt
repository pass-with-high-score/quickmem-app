package com.pwhs.quickmem.data.dto.flashcard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FlipFlashCardDto(
    @SerialName("flipStatus")
    val flipStatus: String
)