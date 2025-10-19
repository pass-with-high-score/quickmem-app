package com.pwhs.quickmem.presentation.app.ai_generative

import androidx.annotation.StringRes

sealed class AIGenerativeUiEvent {
    data class Error(@param:StringRes val message: Int) : AIGenerativeUiEvent()
    data class EarnedCoins(@param:StringRes val message: Int) : AIGenerativeUiEvent()
    data class CreatedStudySet(val studySetId: String) : AIGenerativeUiEvent()
}