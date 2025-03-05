package com.pwhs.quickmem.data.dto.study_set

import com.google.gson.annotations.SerializedName
import com.pwhs.quickmem.core.data.enums.DifficultyLevel
import com.pwhs.quickmem.core.data.enums.LanguageCode
import com.pwhs.quickmem.core.data.enums.QuestionType

data class CreateStudySetByAIRequestDto(
    @SerializedName("description")
    val description: String,
    @SerializedName("difficulty")
    val difficulty: String = DifficultyLevel.EASY.level,
    @SerializedName("language")
    val language: String = LanguageCode.EN.code,
    @SerializedName("numberOfFlashcards")
    val numberOfFlashcards: Int = 15,
    @SerializedName("questionType")
    val questionType: String = QuestionType.MULTIPLE_CHOICE.type,
    @SerializedName("title")
    val title: String,
)
