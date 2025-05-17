package com.pwhs.quickmem.presentation.app.home.recent.study_set

sealed class AllRecentAccessStudySetsUiAction() {
    data object RefreshStudySets : AllRecentAccessStudySetsUiAction()
}