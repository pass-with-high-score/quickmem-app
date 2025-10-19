package com.pwhs.quickmem.presentation.app.search

import androidx.annotation.StringRes

sealed class SearchUiEvent {
    data class NavigateToResult(val query: String) : SearchUiEvent()
    data class ShowError(@param:StringRes val error: Int) : SearchUiEvent()
    data object ClearAllSearchRecent : SearchUiEvent()
}