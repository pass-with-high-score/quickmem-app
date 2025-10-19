package com.pwhs.quickmem.presentation.app.folder.detail

import androidx.annotation.StringRes

sealed class FolderDetailUiEvent {
    data object UnAuthorized : FolderDetailUiEvent()
    data object FolderDeleted : FolderDetailUiEvent()
    data object NavigateToEditFolder : FolderDetailUiEvent()
    data class ShowError(@param:StringRes val message: Int) : FolderDetailUiEvent()
    data class OnNavigateToFlipFlashcard(val isGetAll: Boolean) : FolderDetailUiEvent()
    data class OnNavigateToQuiz(val isGetAll: Boolean) : FolderDetailUiEvent()
    data class OnNavigateToTrueFalse(val isGetAll: Boolean) : FolderDetailUiEvent()
    data class OnNavigateToWrite(val isGetAll: Boolean) : FolderDetailUiEvent()
    data object NotFound : FolderDetailUiEvent()
}