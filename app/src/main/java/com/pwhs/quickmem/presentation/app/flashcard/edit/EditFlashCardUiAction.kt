package com.pwhs.quickmem.presentation.app.flashcard.edit

import android.net.Uri


sealed class EditFlashCardUiAction {
    data class FlashcardIdChanged(val flashcardId: String) : EditFlashCardUiAction()
    data class FlashCardTermChanged(val term: String) : EditFlashCardUiAction()
    data class FlashCardDefinitionChanged(val definition: String) : EditFlashCardUiAction()
    data class FlashCardDefinitionImageChanged(val definitionImageUri: Uri?) :
        EditFlashCardUiAction()

    data class FlashCardHintChanged(val hint: String) : EditFlashCardUiAction()
    data class FlashCardExplanationChanged(val explanation: String) : EditFlashCardUiAction()
    data object SaveFlashCard : EditFlashCardUiAction()

    data class ShowHintClicked(val showHint: Boolean) : EditFlashCardUiAction()
    data class ShowExplanationClicked(val showExplanation: Boolean) : EditFlashCardUiAction()
    data class UploadImage(val imageUri: Uri) : EditFlashCardUiAction()
    data class RemoveImage(val imageURL: String) : EditFlashCardUiAction()
    data class OnQueryImageChanged(val query: String) : EditFlashCardUiAction()
    data class OnDefinitionImageChanged(val definitionImageUrl: String): EditFlashCardUiAction()
    data object OnDeleteFlashCard : EditFlashCardUiAction()
}