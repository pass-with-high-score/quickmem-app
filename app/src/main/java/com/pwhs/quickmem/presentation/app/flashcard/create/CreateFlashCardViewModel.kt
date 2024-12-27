package com.pwhs.quickmem.presentation.app.flashcard.create

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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateFlashCardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val flashCardRepository: FlashCardRepository,
    private val uploadImageRepository: UploadImageRepository,
    private val pixaBayRepository: PixaBayRepository,
    private val tokenManager: TokenManager,
    application: Application,
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(CreateFlashCardUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<CreateFlashCardUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var job: Job? = null

    init {
        val studySetId: String = savedStateHandle.get<String>("studySetId") ?: ""
        val studySetColorId: Int = savedStateHandle.get<Int>("studySetColorId") ?: 1
        _uiState.update {
            it.copy(
                studySetId = studySetId,
                studyColorModel = ColorModel.defaultColors.first { it.id == studySetColorId })
        }
    }

    fun onEvent(event: CreateFlashCardUiAction) {
        when (event) {
            is CreateFlashCardUiAction.FlashCardDefinitionChanged -> {
                _uiState.update { it.copy(definition = event.definition) }
            }

            is CreateFlashCardUiAction.FlashCardDefinitionImageChanged -> {
                _uiState.update {
                    it.copy(
                        definitionImageUri = event.definitionImageUri,
                    )
                }
            }

            is CreateFlashCardUiAction.FlashCardExplanationChanged -> {
                _uiState.update { it.copy(explanation = event.explanation) }
            }

            is CreateFlashCardUiAction.FlashCardHintChanged -> {
                _uiState.update { it.copy(hint = event.hint) }
            }

            is CreateFlashCardUiAction.FlashCardTermChanged -> {
                _uiState.update { it.copy(term = event.term) }
            }

            is CreateFlashCardUiAction.SaveFlashCard -> {
                saveFlashCard()
            }

            is CreateFlashCardUiAction.StudySetIdChanged -> {
                _uiState.update { it.copy(studySetId = event.studySetId) }
            }

            is CreateFlashCardUiAction.ShowExplanationClicked -> {
                _uiState.update { it.copy(showExplanation = event.showExplanation) }
            }

            is CreateFlashCardUiAction.ShowHintClicked -> {
                _uiState.update {
                    it.copy(showHint = event.showHint)
                }
            }

            is CreateFlashCardUiAction.UploadImage -> {

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

            is CreateFlashCardUiAction.RemoveImage -> {
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

            is CreateFlashCardUiAction.OnQueryImageChanged -> {
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
                                Timber.e("Error: ${resource.message}")
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

            is CreateFlashCardUiAction.OnDefinitionImageChanged -> {
                _uiState.update {
                    it.copy(
                        definitionImageURL = event.definitionImageUrl,
                    )
                }
            }
        }
    }

    private fun saveFlashCard() {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            flashCardRepository.createFlashCard(
                token,
                _uiState.value.toCreateFlashCardModel()
            ).collect { resource ->
                when (resource) {
                    is Resources.Error -> {
                        Timber.e("Error: ${resource.message}")
                        _uiState.update { it.copy(isLoading = false) }
                    }

                    is Resources.Loading -> {
                        Timber.d("Loading")
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resources.Success -> {
                        Timber.d("FlashCard saved: ${resource.data}")
                        _uiState.update {
                            it.copy(
                                term = "",
                                definition = "",
                                definitionImageURL = null,
                                definitionImageUri = null,
                                hint = null,
                                explanation = null,
                                isCreated = true,
                                isLoading = false
                            )
                        }
                        _uiEvent.send(CreateFlashCardUiEvent.FlashCardSaved)
                    }
                }
            }
        }
    }
}
