package com.pwhs.quickmem.presentation.app.flashcard.edit

import android.net.Uri
import com.pwhs.quickmem.domain.model.flashcard.LanguageModel
import com.pwhs.quickmem.domain.model.flashcard.VoiceModel


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
    data class UploadImage(val imageUri: Uri, val isTerm: Boolean) : EditFlashCardUiAction()
    data class RemoveImage(val imageURL: String, val isTerm: Boolean) : EditFlashCardUiAction()
    data class OnDefinitionImageChanged(val definitionImageUrl: String) : EditFlashCardUiAction()
    data class FlashCardTermImageChanged(val termImageUri: Uri?) : EditFlashCardUiAction()
    data class OnTermImageChanged(val termImageURL: String) : EditFlashCardUiAction()
    data class OnQueryTermImageChanged(val query: String) : EditFlashCardUiAction()
    data class OnQueryDefinitionImageChanged(val query: String) : EditFlashCardUiAction()
    data class OnSelectTermLanguageClicked(val languageModel: LanguageModel) : EditFlashCardUiAction()
    data class OnSelectTermVoiceClicked(val voiceModel: VoiceModel) : EditFlashCardUiAction()
    data class OnSelectDefinitionLanguageClicked(val languageModel: LanguageModel) : EditFlashCardUiAction()
    data class OnSelectDefinitionVoiceClicked(val voiceModel: VoiceModel) : EditFlashCardUiAction()
    data object OnDeleteFlashCard : EditFlashCardUiAction()
}