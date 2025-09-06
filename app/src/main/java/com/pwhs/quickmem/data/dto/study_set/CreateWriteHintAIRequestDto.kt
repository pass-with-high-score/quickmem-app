package com.pwhs.quickmem.data.dto.study_set

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateWriteHintAIRequestDto(
    @SerialName("flashcardId")
    val flashcardId: String,
    @SerialName("studySetTitle")
    val studySetTitle: String,
    @SerialName("studySetDescription")
    val studySetDescription: String,
    @SerialName("question")
    val question: String,
    @SerialName("answer")
    val answer: String,
)
