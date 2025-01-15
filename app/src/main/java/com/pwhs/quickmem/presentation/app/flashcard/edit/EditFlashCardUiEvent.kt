package com.pwhs.quickmem.presentation.app.flashcard.edit

sealed class EditFlashCardUiEvent {
    data object FlashCardSaved : EditFlashCardUiEvent()
    data object FlashCardSaveError : EditFlashCardUiEvent()
    data object FlashCardDeleted : EditFlashCardUiEvent()
}