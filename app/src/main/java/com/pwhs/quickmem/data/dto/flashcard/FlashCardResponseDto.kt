package com.pwhs.quickmem.data.dto.flashcard

import com.google.gson.annotations.SerializedName

data class FlashCardResponseDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("term")
    val term: String,
    @SerializedName("termImageURL")
    val termImageURL: String?,
    @SerializedName("definition")
    val definition: String,
    @SerializedName("definitionImageURL")
    val definitionImageURL: String?,
    @SerializedName("hint")
    val hint: String?,
    @SerializedName("explanation")
    val explanation: String?,
    @SerializedName("studySetId")
    val studySetId: String,
    @SerializedName("rating")
    val rating: String,
    @SerializedName("flipStatus")
    val flipStatus: String,
    @SerializedName("quizStatus")
    val quizStatus: String,
    @SerializedName("isStarred")
    val isStarred: Boolean,
    @SerializedName("termVoiceCode")
    val termVoiceCode: String?,
    @SerializedName("definitionVoiceCode")
    val definitionVoiceCode: String?,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)