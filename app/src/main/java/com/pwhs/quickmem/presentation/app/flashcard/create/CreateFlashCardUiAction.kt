package com.pwhs.quickmem.presentation.app.flashcard.create

import android.net.Uri
import com.pwhs.quickmem.domain.model.flashcard.LanguageModel
import com.pwhs.quickmem.domain.model.flashcard.VoiceModel


sealed class CreateFlashCardUiAction {
    data class StudySetIdChanged(val studySetId: String) : CreateFlashCardUiAction()
    data class FlashCardTermChanged(val term: String) : CreateFlashCardUiAction()
    data class FlashCardDefinitionChanged(val definition: String) : CreateFlashCardUiAction()
    data class FlashCardDefinitionImageChanged(val definitionImageUri: Uri?) :
        CreateFlashCardUiAction()

    data class FlashCardTermImageChanged(val termImageUri: Uri?) : CreateFlashCardUiAction()
    data class OnTermImageChanged(val termImageURL: String) : CreateFlashCardUiAction()

    data class FlashCardHintChanged(val hint: String) : CreateFlashCardUiAction()
    data class FlashCardExplanationChanged(val explanation: String) : CreateFlashCardUiAction()
    data class ShowHintClicked(val showHint: Boolean) : CreateFlashCardUiAction()
    data class ShowExplanationClicked(val showExplanation: Boolean) : CreateFlashCardUiAction()
    data class UploadImage(val imageUri: Uri, val isTerm: Boolean) : CreateFlashCardUiAction()
    data class RemoveImage(val imageURL: String) : CreateFlashCardUiAction()
    data class OnQueryTermImageChanged(val query: String) : CreateFlashCardUiAction()
    data class OnQueryDefinitionImageChanged(val query: String) : CreateFlashCardUiAction()
    data class OnDefinitionImageChanged(val definitionImageUrl: String) :
        CreateFlashCardUiAction()

    data class OnSelectTermLanguageClicked(val languageModel: LanguageModel) :
        CreateFlashCardUiAction()

    data class OnSelectTermVoiceClicked(val voiceModel: VoiceModel) :
        CreateFlashCardUiAction()

    data class OnSelectDefinitionLanguageClicked(val languageModel: LanguageModel) :
        CreateFlashCardUiAction()

    data class OnSelectDefinitionVoiceClicked(val voiceModel: VoiceModel) :
        CreateFlashCardUiAction()

    data object SaveFlashCard : CreateFlashCardUiAction()
}