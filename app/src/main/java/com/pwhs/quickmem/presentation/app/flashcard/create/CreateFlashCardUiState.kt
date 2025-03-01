package com.pwhs.quickmem.presentation.app.flashcard.create

import android.net.Uri
import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.domain.model.flashcard.CreateFlashCardModel
import com.pwhs.quickmem.domain.model.flashcard.LanguageModel
import com.pwhs.quickmem.domain.model.flashcard.VoiceModel
import com.pwhs.quickmem.domain.model.pixabay.SearchImageResponseModel

data class CreateFlashCardUiState(
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
    val previousDefinitionVoiceCode: String = "",
    val previousTermVoiceCode: String = "",
)

fun CreateFlashCardUiState.toCreateFlashCardModel(): CreateFlashCardModel {
    return CreateFlashCardModel(
        studySetId = studySetId,
        term = term.trim(),
        termImageURL = termImageURL,
        termVoiceCode = termVoiceCode?.code,
        definition = definition.trim(),
        definitionImageURL = definitionImageURL,
        definitionVoiceCode = definitionVoiceCode?.code,
        hint = hint?.trim(),
        explanation = explanation?.trim(),
    )
}
