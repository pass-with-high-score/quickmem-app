package com.pwhs.quickmem.presentation.app.library

sealed class LibraryUiEvent() {
    data class Error(val message: String) : LibraryUiEvent()
}
