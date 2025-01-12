package com.pwhs.quickmem.presentation.app.home.subject

sealed class SearchStudySetBySubjectUiAction() {
    data object RefreshStudySets : SearchStudySetBySubjectUiAction()
}