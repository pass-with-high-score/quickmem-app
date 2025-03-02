package com.pwhs.quickmem.presentation.app.flashcard.edit

import android.net.Uri
import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.domain.model.flashcard.EditFlashCardModel
import com.pwhs.quickmem.domain.model.flashcard.LanguageModel
import com.pwhs.quickmem.domain.model.flashcard.VoiceModel
import com.pwhs.quickmem.domain.model.pixabay.SearchImageResponseModel

data class EditFlashCardUiState(
    val flashcardId: String = "",
    val queryImage: String = "",
    val searchImageResponseModel: SearchImageResponseModel? = null,
    val isSearchImageLoading: Boolean = false,
    val studySetId: String = "",
    val term: String = "",
    val termImageUri: Uri? = null,
    val termImageURL: String? = null,
    val termLanguageModel: LanguageModel? = null,
    val termVoiceCode: VoiceModel? = null,
    val termVoicesModel: List<VoiceModel> = emptyList(),
    val definition: String = "",
    val definitionImageUri: Uri? = null,
    val definitionImageURL: String? = null,
    val definitionLanguageModel: LanguageModel? = null,
    val definitionVoiceCode: VoiceModel? = null,
    val definitionVoicesModel: List<VoiceModel> = emptyList(),
    val hint: String? = null,
    val showHint: Boolean = false,
    val explanation: String? = null,
    val showExplanation: Boolean = false,
    val isCreated: Boolean = false,
    val isLoading: Boolean = false,
    val termQueryImage: String = "",
    val termSearchImageResponseModel: SearchImageResponseModel? = null,
    val isSearchTermImageLoading: Boolean = false,
    val definitionQueryImage: String = "",
    val definitionSearchImageResponseModel: SearchImageResponseModel? = null,
    val isSearchDefinitionImageLoading: Boolean = false,
    val studyColorModel: ColorModel? = null,
    val languageModels: List<LanguageModel> = emptyList(),
    val languageLocale: String = "",
    val currentDefinitionVoiceCode: String = "",
    val currentTermVoiceCode: String = "",
)

fun EditFlashCardUiState.toEditFlashCardModel(): EditFlashCardModel {
    return EditFlashCardModel(
        term = term.trim(),
        definition = definition.trim(),
        definitionImageURL = definitionImageURL,
        hint = hint ?: "",
        explanation = explanation ?: "",
        termImageURL = termImageURL,
        termVoiceCode = termVoiceCode?.code,
        definitionVoiceCode = definitionVoiceCode?.code,
    )
}
