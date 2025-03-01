package com.pwhs.quickmem.presentation.app.study_set.detail

sealed class StudySetDetailUiEvent {
    data object NotFound : StudySetDetailUiEvent()
    data object UnAuthorized : StudySetDetailUiEvent()
    data object FlashCardStarred : StudySetDetailUiEvent()
    data object NavigateToEditStudySet : StudySetDetailUiEvent()
    data object NavigateToEditFlashCard : StudySetDetailUiEvent()
    data object StudySetDeleted : StudySetDetailUiEvent()
    data object StudySetProgressReset : StudySetDetailUiEvent()
    data class StudySetCopied(val newStudySetId: String) : StudySetDetailUiEvent()
    data class OnNavigateToFlipFlashcard(val isGetAll: Boolean) : StudySetDetailUiEvent()
    data class OnNavigateToQuiz(val isGetAll: Boolean) : StudySetDetailUiEvent()
    data class OnNavigateToTrueFalse(val isGetAll: Boolean) : StudySetDetailUiEvent()
    data class OnNavigateToWrite(val isGetAll: Boolean) : StudySetDetailUiEvent()
}