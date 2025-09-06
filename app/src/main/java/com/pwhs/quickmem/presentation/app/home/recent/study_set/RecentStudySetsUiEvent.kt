package com.pwhs.quickmem.presentation.app.home.recent.study_set

sealed class RecentStudySetsUiEvent {
    data class Error(val message: String) : RecentStudySetsUiEvent()
}