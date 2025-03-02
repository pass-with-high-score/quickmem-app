package com.pwhs.quickmem.data.dto.flashcard

import com.google.gson.annotations.SerializedName

data class EditFlashCardDto(
    @SerializedName("definition")
    val definition: String,
    @SerializedName("definitionImageURL")
    val definitionImageURL: String? = null,
    @SerializedName("definitionVoiceCode")
    val definitionVoiceCode: String? = null,
    @SerializedName("explanation")
    val explanation: String? = null,
    @SerializedName("hint")
    val hint: String? = null,
    @SerializedName("term")
    val term: String,
    @SerializedName("termImageURL")
    val termImageURL: String? = null,
    @SerializedName("termVoiceCode")
    val termVoiceCode: String? = null,
)