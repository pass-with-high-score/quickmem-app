package com.pwhs.quickmem.presentation.app.library

sealed class LibraryUiAction() {
    data object Refresh : LibraryUiAction()
    data object RefreshStudySets : LibraryUiAction()
    data object RefreshFolders : LibraryUiAction()
}

