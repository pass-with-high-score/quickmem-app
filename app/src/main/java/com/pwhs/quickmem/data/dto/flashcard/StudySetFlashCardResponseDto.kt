package com.pwhs.quickmem.data.dto.flashcard

import com.google.gson.annotations.SerializedName
import com.pwhs.quickmem.core.data.enums.FlipCardStatus
import com.pwhs.quickmem.core.data.enums.QuizStatus
import com.pwhs.quickmem.core.data.enums.Rating
import com.pwhs.quickmem.core.data.enums.TrueFalseStatus
import com.pwhs.quickmem.core.data.enums.WriteStatus

data class StudySetFlashCardResponseDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("term")
    val term: String,
    @SerializedName("definition")
    val definition: String,
    @SerializedName("definitionImageURL")
    val definitionImageURL: String?,
    @SerializedName("hint")
    val hint: String?,
    @SerializedName("explanation")
    val explanation: String?,
    @SerializedName("rating")
    val rating: String = Rating.NOT_STUDIED.name,
    @SerializedName("flipStatus")
    val flipStatus: String = FlipCardStatus.NONE.name,
    @SerializedName("quizStatus")
    val quizStatus: String = QuizStatus.NONE.status,
    @SerializedName("trueFalseStatus")
    val trueFalseStatus: String = TrueFalseStatus.NONE.status,
    @SerializedName("writeStatus")
    val writeStatus: String = WriteStatus.NONE.status,
    @SerializedName("isStarred")
    val isStarred: Boolean,
    @SerializedName("isAIGenerated")
    val isAIGenerated: Boolean,
    @SerializedName("termVoiceCode")
    val termVoiceCode: String?,
    @SerializedName("definitionVoiceCode")
    val definitionVoiceCode: String?,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)