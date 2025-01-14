package com.pwhs.quickmem.presentation.app.ai_generative

import androidx.annotation.StringRes
import com.pwhs.quickmem.core.data.enums.DifficultyLevel
import com.pwhs.quickmem.core.data.enums.LanguageCode
import com.pwhs.quickmem.core.data.enums.QuestionType
import com.revenuecat.purchases.CustomerInfo

data class ExploreUiState(
    val isLoading: Boolean = false,
    val ownerId: String = "",
    val customerInfo: CustomerInfo? = null,
    val description: String = "",
    val difficulty: DifficultyLevel = DifficultyLevel.EASY,
    val language: String = LanguageCode.EN.code,
    val numberOfFlashcards: Int = 15,
    val questionType: QuestionType = QuestionType.MULTIPLE_CHOICE,
    val title: String = "",
    @StringRes val errorMessage: Int? = null,
    val coins: Int = 0,
)