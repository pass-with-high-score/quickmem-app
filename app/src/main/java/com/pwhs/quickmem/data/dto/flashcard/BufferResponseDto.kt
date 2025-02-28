package com.pwhs.quickmem.data.dto.flashcard

import com.google.gson.annotations.SerializedName

data class BufferResponseDto(
    @SerializedName("type")
    val type: String,
    @SerializedName("data")
    val data: List<Int>
)
