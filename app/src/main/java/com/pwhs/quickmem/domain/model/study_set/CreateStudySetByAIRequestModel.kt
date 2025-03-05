package com.pwhs.quickmem.domain.model.study_set

import com.pwhs.quickmem.core.data.enums.DifficultyLevel
import com.pwhs.quickmem.core.data.enums.LanguageCode
import com.pwhs.quickmem.core.data.enums.QuestionType

data class CreateStudySetByAIRequestModel(
    val description: String,
    val difficulty: String = DifficultyLevel.EASY.level,
    val language: String = LanguageCode.EN.code,
    val numberOfFlashcards: Int = 15,
    val questionType: String = QuestionType.MULTIPLE_CHOICE.type,
    val title: String,
)
