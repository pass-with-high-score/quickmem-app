package com.pwhs.quickmem.data.dto.flashcard

import com.google.gson.annotations.SerializedName

data class LanguageDto(
    @SerializedName("code")
    val code: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("voiceAvailableCount")
    val voiceAvailableCount: Int,
)
