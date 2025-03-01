package com.pwhs.quickmem.presentation.app.flashcard.create

import android.app.Application
import android.net.Uri
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
        val previousDefinitionVoiceCode: String =
            savedStateHandle.get<String>("previousDefinitionVoiceCode") ?: ""
        val previousTermVoiceCode: String =
            savedStateHandle.get<String>("previousTermVoiceCode") ?: ""
        _uiState.update {
            it.copy(
                studySetId = studySetId,
                studyColorModel = ColorModel.defaultColors.first { it.id == studySetColorId },
                previousDefinitionVoiceCode = previousDefinitionVoiceCode,
                previousTermVoiceCode = previousTermVoiceCode
            )
        }
        getLanguages()
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
                onUploadImage(event.imageUri, event.isTerm)
            }

            is CreateFlashCardUiAction.RemoveImage -> {
                removeImage(event.imageURL)
            }

            is CreateFlashCardUiAction.OnQueryTermImageChanged -> {
                onSearchImage(event.query)
            }

            is CreateFlashCardUiAction.OnDefinitionImageChanged -> {
                _uiState.update {
                    it.copy(
                        definitionImageURL = event.definitionImageUrl,
                    )
                }
            }

            is CreateFlashCardUiAction.OnSelectTermLanguageClicked -> {
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

            is CreateFlashCardUiAction.OnSelectTermVoiceClicked -> {
                _uiState.update {
                    it.copy(
                        termVoiceCode = event.voiceModel
                    )
                }
            }

            is CreateFlashCardUiAction.FlashCardTermImageChanged -> {
                _uiState.update {
                    it.copy(
                        termImageUri = event.termImageUri,
                    )
                }
            }

            is CreateFlashCardUiAction.OnTermImageChanged -> {
                _uiState.update {
                    it.copy(
                        termImageURL = event.termImageURL,
                    )
                }
            }

            is CreateFlashCardUiAction.OnSelectDefinitionLanguageClicked -> {
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

            is CreateFlashCardUiAction.OnSelectDefinitionVoiceClicked -> {
                _uiState.update {
                    it.copy(
                        definitionVoiceCode = event.voiceModel
                    )
                }
            }

            is CreateFlashCardUiAction.OnQueryDefinitionImageChanged -> {
                onSearchImage(event.query, isTerm = false)
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

    private fun getLanguages(isInit: Boolean = true) {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            val termLanguageCode =
                _uiState.value.previousTermVoiceCode.split("-").take(2).joinToString("-")
            val definitionLanguageCode =
                _uiState.value.previousDefinitionVoiceCode.split("-").take(2).joinToString("-")
            flashCardRepository.getLanguages(token = token).collect { resource ->
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
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            flashCardRepository.getVoices(token = token, languageCode = languageCode)
                .collect { resource ->
                    when (resource) {
                        is Resources.Success -> {
                            if (isTerm) {
                                _uiState.update {
                                    it.copy(
                                        termVoicesModel = resource.data ?: emptyList(),
                                        termVoiceCode = if (isInit) {
                                            resource.data?.firstOrNull { it.code == _uiState.value.previousTermVoiceCode }
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
                                            resource.data?.firstOrNull { it.code == _uiState.value.previousDefinitionVoiceCode }
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

    private fun removeImage(imageURL: String, isTerm: Boolean = true) {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            uploadImageRepository.removeImage(token, imageURL)
                .collect { resource ->
                    when (resource) {
                        is Resources.Success -> {
                            _uiState.update {
                                it.copy(
                                    definitionImageURL = if (!isTerm) null else it.definitionImageURL,
                                    definitionImageUri = if (!isTerm) null else it.definitionImageUri,
                                    termImageURL = if (isTerm) null else it.termImageURL,
                                    termImageUri = if (isTerm) null else it.termImageUri,
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
                token = tokenManager.accessToken.firstOrNull() ?: "",
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
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            uploadImageRepository
                .uploadImage(token, imageUri)
                .collect { resource ->
                    when (resource) {
                        is Resources.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    termImageUri = if (isTerm) imageUri else it.termImageUri,
                                    definitionImageUri = if (!isTerm) imageUri else it.definitionImageUri,
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
