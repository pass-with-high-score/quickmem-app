package com.pwhs.quickmem.presentation.app.home.recent.folder

sealed class AllRecentAccessFoldersUiEvent {
    data class Error(val message: String) : AllRecentAccessFoldersUiEvent()
}