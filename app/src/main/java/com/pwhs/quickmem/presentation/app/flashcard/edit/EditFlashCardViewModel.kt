package com.pwhs.quickmem.presentation.app.flashcard.edit

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.domain.repository.FlashCardRepository
import com.pwhs.quickmem.domain.repository.PixaBayRepository
import com.pwhs.quickmem.domain.repository.UploadImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditFlashCardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val flashCardRepository: FlashCardRepository,
    private val uploadImageRepository: UploadImageRepository,
    private val pixaBayRepository: PixaBayRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditFlashCardUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<EditFlashCardUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var job: Job? = null

    init {
        val flashcardId: String = savedStateHandle.get<String>("flashcardId") ?: ""
        val term: String = savedStateHandle.get<String>("term") ?: ""
        val termImageUrl = savedStateHandle.get<String>("termImageUrl") ?: ""
        val termVoiceCode = savedStateHandle.get<String>("termVoiceCode") ?: ""
        val definition: String = savedStateHandle.get<String>("definition") ?: ""
        val definitionImageUrl: String = savedStateHandle.get<String>("definitionImageUrl") ?: ""
        val definitionVoiceCode = savedStateHandle.get<String>("definitionVoiceCode") ?: ""
        val hint: String = savedStateHandle.get<String>("hint") ?: ""
        val explanation: String = savedStateHandle.get<String>("explanation") ?: ""
        val studySetColorId: Int = savedStateHandle.get<Int>("studySetColorId") ?: 1
        _uiState.update {
            it.copy(
                flashcardId = flashcardId,
                term = term,
                termImageURL = termImageUrl,
                currentTermVoiceCode = termVoiceCode,
                definition = definition,
                definitionImageURL = definitionImageUrl,
                currentDefinitionVoiceCode = definitionVoiceCode,
                hint = hint,
                explanation = explanation,
                studyColorModel = ColorModel.defaultColors.first { it.id == studySetColorId }
            )
        }
        getLanguages()
    }

    fun onEvent(event: EditFlashCardUiAction) {
        when (event) {
            is EditFlashCardUiAction.FlashCardDefinitionChanged -> {
                _uiState.update { it.copy(definition = event.definition) }
            }

            is EditFlashCardUiAction.FlashCardDefinitionImageChanged -> {
                _uiState.update { it.copy(definitionImageUri = event.definitionImageUri) }
            }

            is EditFlashCardUiAction.FlashCardExplanationChanged -> {
                _uiState.update { it.copy(explanation = event.explanation) }
            }

            is EditFlashCardUiAction.FlashCardHintChanged -> {
                _uiState.update { it.copy(hint = event.hint) }
            }

            is EditFlashCardUiAction.FlashCardTermChanged -> {
                _uiState.update { it.copy(term = event.term) }
            }

            is EditFlashCardUiAction.SaveFlashCard -> {
                saveFlashCard()
            }

            is EditFlashCardUiAction.FlashcardIdChanged -> {
                _uiState.update { it.copy(flashcardId = event.flashcardId) }
            }

            is EditFlashCardUiAction.ShowExplanationClicked -> {
                _uiState.update { it.copy(showExplanation = event.showExplanation) }
            }

            is EditFlashCardUiAction.ShowHintClicked -> {
                _uiState.update {
                    it.copy(showHint = event.showHint)
                }
            }

            is EditFlashCardUiAction.UploadImage -> {
                onUploadImage(event.imageUri, event.isTerm)
            }

            is EditFlashCardUiAction.RemoveImage -> {
                viewModelScope.launch {
                    uploadImageRepository.removeImage(event.imageURL)
                        .collect { resource ->
                            when (resource) {
                                is Resources.Success -> {
                                    _uiState.update {
                                        it.copy(
                                            definitionImageURL = "",
                                            definitionImageUri = null,
                                            isLoading = false
                                        )
                                    }
                                }

                                is Resources.Error -> {
                                    _uiState.update { it.copy(isLoading = false) }
                                }

                                is Resources.Loading -> {
                                    _uiState.update {
                                        it.copy(isLoading = true)
                                    }
                                }
                            }
                        }
                }
            }

            is EditFlashCardUiAction.OnDefinitionImageChanged -> {
                _uiState.update {
                    it.copy(
                        definitionImageURL = event.definitionImageUrl,
                    )
                }
            }

            is EditFlashCardUiAction.OnDeleteFlashCard -> {
                deleteFlashCard()
            }

            is EditFlashCardUiAction.FlashCardTermImageChanged -> {
                _uiState.update { it.copy(termImageUri = event.termImageUri) }
            }

            is EditFlashCardUiAction.OnQueryDefinitionImageChanged -> {
                onSearchImage(event.query, false)
            }

            is EditFlashCardUiAction.OnQueryTermImageChanged -> {
                onSearchImage(event.query)
            }

            is EditFlashCardUiAction.OnSelectDefinitionLanguageClicked -> {
                _uiState.update {
                    it.copy(
                        definitionLanguageModel = event.languageModel
                    )
                }

                getVoices(
                    isTerm = false,
                    languageCode = event.languageModel.code,
                    isInit = false
                )
            }

            is EditFlashCardUiAction.OnSelectDefinitionVoiceClicked -> {
                _uiState.update {
                    it.copy(
                        definitionVoiceCode = event.voiceModel
                    )
                }
            }

            is EditFlashCardUiAction.OnSelectTermLanguageClicked -> {
                _uiState.update {
                    it.copy(
                        termLanguageModel = event.languageModel
                    )
                }
                getVoices(
                    isTerm = true,
                    languageCode = event.languageModel.code,
                    isInit = false
                )
            }

            is EditFlashCardUiAction.OnSelectTermVoiceClicked -> {
                _uiState.update {
                    it.copy(
                        termVoiceCode = event.voiceModel
                    )
                }
            }

            is EditFlashCardUiAction.OnTermImageChanged -> {
                _uiState.update {
                    it.copy(
                        termImageURL = event.termImageURL,
                    )
                }
            }
        }
    }

    private fun saveFlashCard() {
        viewModelScope.launch {
            val editFlashCardModel = _uiState.value.toEditFlashCardModel()
            flashCardRepository.updateFlashCard(
                id = _uiState.value.flashcardId,
                editFlashCardModel = editFlashCardModel
            ).collect { resource ->
                when (resource) {
                    is Resources.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }

                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(
                                term = "",
                                termImageURL = null,
                                termImageUri = null,
                                termVoiceCode = null,
                                definition = "",
                                definitionImageURL = null,
                                definitionImageUri = null,
                                definitionVoiceCode = null,
                                hint = null,
                                explanation = null,
                                isLoading = false
                            )
                        }
                        _uiEvent.send(EditFlashCardUiEvent.FlashCardSaved)
                    }
                }
            }
        }
    }

    private fun deleteFlashCard() {
        viewModelScope.launch {
            flashCardRepository.deleteFlashCard(id = _uiState.value.flashcardId)
                .collect { resource ->
                    when (resource) {
                        is Resources.Error -> {
                            _uiState.update { it.copy(isLoading = false) }
                        }

                        is Resources.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }

                        is Resources.Success -> {
                            _uiState.update {
                                it.copy(
                                    term = "",
                                    definition = "",
                                    definitionImageURL = null,
                                    definitionImageUri = null,
                                    hint = null,
                                    explanation = null,
                                    isLoading = false
                                )
                            }
                            _uiEvent.send(EditFlashCardUiEvent.FlashCardDeleted)
                        }
                    }
                }
        }
    }


    private fun getLanguages(isInit: Boolean = true) {
        viewModelScope.launch {
            val termLanguageCode =
                _uiState.value.currentTermVoiceCode.split("-").take(2).joinToString("-")
            val definitionLanguageCode =
                _uiState.value.currentDefinitionVoiceCode.split("-").take(2).joinToString("-")
            flashCardRepository.getLanguages().collect { resource ->
                when (resource) {
                    is Resources.Success -> {
                        _uiState.update {
                            val termSelectLanguage =
                                resource.data?.firstOrNull { it.code.contains(termLanguageCode) }
                            val definitionLanguage =
                                resource.data?.firstOrNull { it.code.contains(definitionLanguageCode) }

                            it.copy(
                                languageModels = resource.data ?: emptyList(),
                                termLanguageModel = termSelectLanguage
                                    ?: resource.data?.firstOrNull(),
                                definitionLanguageModel = definitionLanguage
                                    ?: resource.data?.firstOrNull()
                            )
                        }.also {
                            getVoices(
                                isTerm = true,
                                languageCode = _uiState.value.termLanguageModel?.code ?: "",
                                isInit = isInit
                            )
                            getVoices(
                                isTerm = false,
                                languageCode = _uiState.value.definitionLanguageModel?.code ?: "",
                                isInit = isInit
                            )

                        }

                    }

                    is Resources.Error -> {
                        Timber.e("Error: ${resource.message}")
                    }

                    is Resources.Loading -> {
                        Timber.d("Loading")
                    }
                }
            }
        }
    }

    private fun getVoices(isTerm: Boolean, languageCode: String, isInit: Boolean) {
        viewModelScope.launch {
            flashCardRepository.getVoices(languageCode = languageCode)
                .collect { resource ->
                    when (resource) {
                        is Resources.Success -> {
                            if (isTerm) {
                                _uiState.update {
                                    it.copy(
                                        termVoicesModel = resource.data ?: emptyList(),
                                        termVoiceCode = if (isInit) {
                                            resource.data?.firstOrNull { it.code == _uiState.value.currentTermVoiceCode }
                                        } else {
                                            null
                                        }
                                    )
                                }
                            } else {
                                _uiState.update {
                                    it.copy(
                                        definitionVoicesModel = resource.data ?: emptyList(),
                                        definitionVoiceCode = if (isInit) {
                                            resource.data?.firstOrNull { it.code == _uiState.value.currentDefinitionVoiceCode }
                                        } else {
                                            null
                                        }
                                    )
                                }
                            }
                        }

                        is Resources.Error -> {
                            Timber.e("Error: ${resource.message}")
                        }

                        is Resources.Loading -> {
                            Timber.d("Loading")
                        }
                    }
                }
        }
    }

    private fun onSearchImage(query: String, isTerm: Boolean = true) {
        if (isTerm) {
            _uiState.update {
                it.copy(
                    termQueryImage = query,
                    isSearchTermImageLoading = true
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    definitionQueryImage = query,
                    isSearchDefinitionImageLoading = true
                )
            }
        }
        if (query.isEmpty()) {
            return
        }

        job?.cancel()
        job = viewModelScope.launch {
            pixaBayRepository.searchImages(
                query = query
            ).collect { resource ->
                when (resource) {
                    is Resources.Success -> {
                        if (isTerm) {
                            _uiState.update {
                                it.copy(
                                    termSearchImageResponseModel = resource.data,
                                    isSearchTermImageLoading = false
                                )
                            }
                        } else {
                            _uiState.update {
                                it.copy(
                                    definitionSearchImageResponseModel = resource.data,
                                    isSearchDefinitionImageLoading = false
                                )
                            }
                        }
                    }

                    is Resources.Error -> {
                        Timber.e("Error: ${resource.message}")
                        if (isTerm) {
                            _uiState.update {
                                it.copy(
                                    termSearchImageResponseModel = null,
                                    isSearchTermImageLoading = false
                                )
                            }
                        } else {
                            _uiState.update {
                                it.copy(
                                    definitionSearchImageResponseModel = null,
                                    isSearchDefinitionImageLoading = false
                                )
                            }
                        }
                    }

                    is Resources.Loading -> {
                        if (isTerm) {
                            _uiState.update {
                                it.copy(
                                    isSearchTermImageLoading = true
                                )
                            }
                        } else {
                            _uiState.update {
                                it.copy(
                                    isSearchDefinitionImageLoading = true
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onUploadImage(imageUri: Uri, isTerm: Boolean = true) {
        viewModelScope.launch {
            uploadImageRepository
                .uploadImage(imageUri = imageUri)
                .collect { resource ->
                    when (resource) {
                        is Resources.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    termImageURL = if (isTerm) resource.data?.url else it.termImageURL,
                                    termImageUri = if (isTerm) null else it.termImageUri,
                                    definitionImageURL = if (!isTerm) resource.data?.url else it.definitionImageURL,
                                    definitionImageUri = if (!isTerm) null else it.definitionImageUri
                                )
                            }
                        }

                        is Resources.Error -> {
                            Timber.e("Error: ${resource.message}")
                            _uiState.update { it.copy(isLoading = false) }
                        }

                        is Resources.Loading -> {
                            _uiState.update {
                                it.copy(isLoading = true)
                            }
                        }
                    }
                }
        }
    }
}
