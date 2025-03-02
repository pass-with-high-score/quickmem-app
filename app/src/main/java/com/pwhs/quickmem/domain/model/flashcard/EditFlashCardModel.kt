package com.pwhs.quickmem.domain.model.flashcard

data class EditFlashCardModel(
    val definition: String,
    val definitionImageURL: String? = null,
    val definitionVoiceCode: String? = null,
    val explanation: String? = null,
    val hint: String? = null,
    val term: String,
    val termImageURL: String? = null,
    val termVoiceCode: String? = null,
)