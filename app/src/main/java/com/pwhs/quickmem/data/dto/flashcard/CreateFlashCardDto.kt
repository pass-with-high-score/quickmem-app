package com.pwhs.quickmem.data.dto.flashcard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateFlashCardDto(
    @SerialName("term")
    val term: String,
    @SerialName("termImageURL")
    val termImageURL: String? = null,
    @SerialName("termVoiceCode")
    val termVoiceCode: String? = null,
    @SerialName("definition")
    val definition: String,
    @SerialName("definitionImageURL")
    val definitionImageURL: String? = null,
    @SerialName("definitionVoiceCode")
    val definitionVoiceCode: String? = null,
    @SerialName("explanation")
    val explanation: String? = null,
    @SerialName("hint")
    val hint: String? = null,
    @SerialName("studySetId")
    val studySetId: String,
)