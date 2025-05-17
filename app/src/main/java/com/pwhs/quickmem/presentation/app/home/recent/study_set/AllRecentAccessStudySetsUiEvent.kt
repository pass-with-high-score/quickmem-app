package com.pwhs.quickmem.presentation.app.home.recent.study_set

sealed class AllRecentAccessStudySetsUiEvent {
    data class Error(val message: String) : AllRecentAccessStudySetsUiEvent()
}