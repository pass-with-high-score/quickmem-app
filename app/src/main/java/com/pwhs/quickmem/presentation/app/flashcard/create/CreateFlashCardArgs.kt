package com.pwhs.quickmem.presentation.app.flashcard.create

import kotlinx.serialization.Serializable

@Serializable
data class CreateFlashCardArgs (
    val studySetId: String,
    val studySetColorId: Int,
    val previousDefinitionVoiceCode: String,
    val previousTermVoiceCode: String,
)