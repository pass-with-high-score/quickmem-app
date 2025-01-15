package com.pwhs.quickmem.presentation.app.flashcard.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.core.datastore.TokenManager
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditFlashCardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val flashCardRepository: FlashCardRepository,
    private val uploadImageRepository: UploadImageRepository,
    private val pixaBayRepository: PixaBayRepository,
    private val tokenManager: TokenManager,
    application: Application,
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(EditFlashCardUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<EditFlashCardUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var job: Job? = null

    init {
        val flashcardId: String = savedStateHandle.get<String>("flashcardId") ?: ""
        val term: String = savedStateHandle.get<String>("term") ?: ""
        val definition: String = savedStateHandle.get<String>("definition") ?: ""
        val definitionImageUrl: String = savedStateHandle.get<String>("definitionImageUrl") ?: ""
        val hint: String = savedStateHandle.get<String>("hint") ?: ""
        val explanation: String = savedStateHandle.get<String>("explanation") ?: ""
        val studySetColorId: Int = savedStateHandle.get<Int>("studySetColorId") ?: 1
        _uiState.update {
            it.copy(
                flashcardId = flashcardId,
                term = term,
                definition = definition,
                definitionImageURL = definitionImageUrl,
                hint = hint,
                explanation = explanation,
                studyColorModel = ColorModel.defaultColors.first { it.id == studySetColorId }
            )
        }
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

                viewModelScope.launch {
                    val token = tokenManager.accessToken.firstOrNull() ?: ""
                    uploadImageRepository.uploadImage(token, event.imageUri)
                        .collect { resource ->
                            when (resource) {
                                is Resources.Success -> {
                                    _uiState.update {
                                        it.copy(
                                            definitionImageURL = resource.data!!.url,
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

            is EditFlashCardUiAction.RemoveImage -> {
                viewModelScope.launch {
                    val token = tokenManager.accessToken.firstOrNull() ?: ""
                    uploadImageRepository.removeImage(token, event.imageURL)
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

            is EditFlashCardUiAction.OnQueryImageChanged -> {
                _uiState.update {
                    it.copy(
                        queryImage = event.query,
                        isSearchImageLoading = true
                    )
                }
                if (event.query.length < 3) {
                    return
                }

                job?.cancel()
                job = viewModelScope.launch {
                    pixaBayRepository.searchImages(
                        token = tokenManager.accessToken.firstOrNull() ?: "",
                        query = event.query
                    ).collect { resource ->
                        when (resource) {
                            is Resources.Success -> {
                                _uiState.update {
                                    it.copy(
                                        searchImageResponseModel = resource.data,
                                        isSearchImageLoading = false
                                    )
                                }
                            }

                            is Resources.Error -> {
                                _uiState.update {
                                    it.copy(
                                        searchImageResponseModel = null,
                                        isSearchImageLoading = false
                                    )
                                }
                            }

                            is Resources.Loading -> {
                                _uiState.update {
                                    it.copy(
                                        isSearchImageLoading = true
                                    )
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
        }
    }

    private fun saveFlashCard() {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            val editFlashCardModel = _uiState.value.toEditFlashCardModel()
            flashCardRepository.updateFlashCard(
                token,
                _uiState.value.flashcardId,
                editFlashCardModel
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
                                definition = "",
                                definitionImageURL = null,
                                definitionImageUri = null,
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
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            flashCardRepository.deleteFlashCard(token, _uiState.value.flashcardId)
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
}
