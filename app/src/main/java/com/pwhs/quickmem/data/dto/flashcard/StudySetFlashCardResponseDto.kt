package com.pwhs.quickmem.data.dto.flashcard

import kotlinx.serialization.SerialName
import com.pwhs.quickmem.core.data.enums.FlipCardStatus
import com.pwhs.quickmem.core.data.enums.QuizStatus
import com.pwhs.quickmem.core.data.enums.Rating
import com.pwhs.quickmem.core.data.enums.TrueFalseStatus
import com.pwhs.quickmem.core.data.enums.WriteStatus
import kotlinx.serialization.Serializable

@Serializable
data class StudySetFlashCardResponseDto(
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
    @SerialName("rating")
    val rating: String = Rating.NOT_STUDIED.name,
    @SerialName("flipStatus")
    val flipStatus: String = FlipCardStatus.NONE.name,
    @SerialName("quizStatus")
    val quizStatus: String = QuizStatus.NONE.status,
    @SerialName("trueFalseStatus")
    val trueFalseStatus: String = TrueFalseStatus.NONE.status,
    @SerialName("writeStatus")
    val writeStatus: String = WriteStatus.NONE.status,
    @SerialName("isStarred")
    val isStarred: Boolean,
    @SerialName("isAIGenerated")
    val isAIGenerated: Boolean,
    @SerialName("termVoiceCode")
    val termVoiceCode: String?,
    @SerialName("definitionVoiceCode")
    val definitionVoiceCode: String?,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String
)