package com.pwhs.quickmem.data.dto.flashcard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FlashCardResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("term")
    val term: String,
    @SerialName("termImageURL")
    val termImageURL: String?,
    @SerialName("definition")
    val definition: String,
    @SerialName("definitionImageURL")
    val definitionImageURL: String?,
    @SerialName("hint")
    val hint: String?,
    @SerialName("explanation")
    val explanation: String?,
    @SerialName("studySetId")
    val studySetId: String,
    @SerialName("rating")
    val rating: String,
    @SerialName("flipStatus")
    val flipStatus: String,
    @SerialName("quizStatus")
    val quizStatus: String,
    @SerialName("isStarred")
    val isStarred: Boolean,
    @SerialName("termVoiceCode")
    val termVoiceCode: String?,
    @SerialName("definitionVoiceCode")
    val definitionVoiceCode: String?,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String
)