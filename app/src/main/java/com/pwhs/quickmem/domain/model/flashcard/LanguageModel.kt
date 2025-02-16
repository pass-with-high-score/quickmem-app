package com.pwhs.quickmem.domain.model.flashcard

data class LanguageModel(
    val code: String,
    val name: String,
    val voiceAvailableCount: Int,
    val flag: String,
    val country: String,
)
