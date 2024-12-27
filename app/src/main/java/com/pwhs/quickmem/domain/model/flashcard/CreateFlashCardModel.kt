package com.pwhs.quickmem.domain.model.flashcard

data class CreateFlashCardModel(
    val definition: String,
    val definitionImageURL: String? = null,
    val explanation: String? = null,
    val hint: String? = null,
    val studySetId: String,
    val term: String,
)