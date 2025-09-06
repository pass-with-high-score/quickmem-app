package com.pwhs.quickmem.data.dto.flashcard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BufferResponseDto(
    @SerialName("type")
    val type: String,
    @SerialName("data")
    val data: List<Int>
)
