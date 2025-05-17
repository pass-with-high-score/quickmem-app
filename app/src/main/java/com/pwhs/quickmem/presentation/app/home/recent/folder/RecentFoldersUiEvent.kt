package com.pwhs.quickmem.presentation.app.home.recent.folder

sealed class RecentFoldersUiEvent {
    data class Error(val message: String) : RecentFoldersUiEvent()
}