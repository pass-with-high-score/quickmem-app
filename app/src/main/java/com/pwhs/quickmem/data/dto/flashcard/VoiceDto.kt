package com.pwhs.quickmem.data.dto.flashcard

import com.google.gson.annotations.SerializedName

data class VoiceDto(
    @SerializedName("code")
    val code: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("name")
    val name: String,
)
