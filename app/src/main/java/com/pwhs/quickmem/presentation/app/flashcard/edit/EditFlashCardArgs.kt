package com.pwhs.quickmem.presentation.app.flashcard.edit

import kotlinx.serialization.Serializable

@Serializable
data class EditFlashCardArgs(
    val flashcardId: String,
    val term: String,
    val termImageUrl: String,
    val termVoiceCode: String,
    val definition: String,
    val definitionImageUrl: String,
    val definitionVoiceCode: String,
    val hint: String,
    val explanation: String,
    val studySetColorId: Int,
)