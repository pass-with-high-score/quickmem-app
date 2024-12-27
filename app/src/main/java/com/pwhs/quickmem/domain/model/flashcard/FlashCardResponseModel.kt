package com.pwhs.quickmem.domain.model.flashcard

data class FlashCardResponseModel(
    val id: String,
    val term: String,
    val termImageURL: String?,
    val definition: String,
    val definitionImageURL: String?,
    val hint: String?,
    val explanation: String?,
    val studySetId: String,
    val rating: String,
    val flipStatus: String,
    val isStarred: Boolean,
    val quizStatus: String,
    var isAnswered: Boolean = false,
    val createdAt: String,
    val updatedAt: String,
)
