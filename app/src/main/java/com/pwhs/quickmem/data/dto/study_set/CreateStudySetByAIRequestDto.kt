package com.pwhs.quickmem.data.dto.study_set

import kotlinx.serialization.SerialName
import com.pwhs.quickmem.core.data.enums.DifficultyLevel
import com.pwhs.quickmem.core.data.enums.LanguageCode
import com.pwhs.quickmem.core.data.enums.QuestionType
import kotlinx.serialization.Serializable

@Serializable
data class CreateStudySetByAIRequestDto(
    @SerialName("description")
    val description: String,
    @SerialName("difficulty")
    val difficulty: String = DifficultyLevel.EASY.level,
    @SerialName("language")
    val language: String = LanguageCode.EN.code,
    @SerialName("numberOfFlashcards")
    val numberOfFlashcards: Int = 15,
    @SerialName("questionType")
    val questionType: String = QuestionType.MULTIPLE_CHOICE.type,
    @SerialName("title")
    val title: String,
)
