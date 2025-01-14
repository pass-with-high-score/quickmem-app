package com.pwhs.quickmem.presentation.app.ai_generative

import com.pwhs.quickmem.core.data.enums.DifficultyLevel
import com.pwhs.quickmem.core.data.enums.QuestionType

sealed class AIGenerativeUiAction {
    data object RefreshTopStreaks : AIGenerativeUiAction()
    data class OnTitleChanged(val title: String) : AIGenerativeUiAction()
    data class OnDescriptionChanged(val description: String) : AIGenerativeUiAction()
    data class OnNumberOfFlashcardsChange(val numberOfCards: Int) : AIGenerativeUiAction()
    data class OnLanguageChanged(val language: String) : AIGenerativeUiAction()
    data class OnQuestionTypeChanged(val questionType: QuestionType) : AIGenerativeUiAction()
    data class OnDifficultyLevelChanged(val difficultyLevel: DifficultyLevel) : AIGenerativeUiAction()
    data object OnCreateStudySet : AIGenerativeUiAction()
    data object OnEarnCoins : AIGenerativeUiAction()
}