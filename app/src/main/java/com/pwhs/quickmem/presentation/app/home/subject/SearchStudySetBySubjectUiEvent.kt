package com.pwhs.quickmem.presentation.app.home.subject

sealed class SearchStudySetBySubjectUiEvent {
    data class Error(val message: String) : SearchStudySetBySubjectUiEvent()
}