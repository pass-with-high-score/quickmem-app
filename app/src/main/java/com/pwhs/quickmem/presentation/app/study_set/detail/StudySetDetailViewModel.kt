package com.pwhs.quickmem.presentation.app.study_set.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.pwhs.quickmem.core.data.enums.LearnMode
import com.pwhs.quickmem.core.data.enums.ResetType
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.data.mapper.flashcard.toByteArray
import com.pwhs.quickmem.domain.model.study_set.SaveRecentAccessStudySetRequestModel
import com.pwhs.quickmem.domain.repository.FlashCardRepository
import com.pwhs.quickmem.domain.repository.StudySetRepository
import com.pwhs.quickmem.domain.repository.StudyTimeRepository
import com.pwhs.quickmem.presentation.app.study_set.detail.StudySetDetailUiEvent.FlashCardDeleted
import com.pwhs.quickmem.presentation.app.study_set.detail.StudySetDetailUiEvent.NavigateToEditFlashCard
import com.pwhs.quickmem.presentation.app.study_set.detail.StudySetDetailUiEvent.NavigateToEditStudySet
import com.pwhs.quickmem.presentation.app.study_set.detail.StudySetDetailUiEvent.NotFound
import com.pwhs.quickmem.presentation.app.study_set.detail.StudySetDetailUiEvent.OnNavigateToFlipFlashcard
import com.pwhs.quickmem.presentation.app.study_set.detail.StudySetDetailUiEvent.OnNavigateToQuiz
import com.pwhs.quickmem.presentation.app.study_set.detail.StudySetDetailUiEvent.OnNavigateToTrueFalse
import com.pwhs.quickmem.presentation.app.study_set.detail.StudySetDetailUiEvent.OnNavigateToWrite
import com.pwhs.quickmem.presentation.app.study_set.detail.StudySetDetailUiEvent.StudySetCopied
import com.pwhs.quickmem.presentation.app.study_set.detail.StudySetDetailUiEvent.StudySetDeleted
import com.pwhs.quickmem.presentation.app.study_set.detail.StudySetDetailUiEvent.StudySetProgressReset
import com.pwhs.quickmem.presentation.app.study_set.detail.StudySetDetailUiEvent.UnAuthorized
import com.pwhs.quickmem.utils.AudioExtension
import com.pwhs.quickmem.utils.toColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StudySetDetailViewModel @Inject constructor(
    private val studySetRepository: StudySetRepository,
    private val flashCardRepository: FlashCardRepository,
    private val studyTimeRepository: StudyTimeRepository,
    private val tokenManager: TokenManager,
    private val appManager: AppManager,
    private val firebaseAnalytics: FirebaseAnalytics,
    savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(StudySetDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<StudySetDetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var job: Job? = null

    init {
        val id: String = savedStateHandle.get<String>("id") ?: ""
        val code: String = savedStateHandle.get<String>("code") ?: ""
        _uiState.update { it.copy(id = id, linkShareCode = code) }
        initData()
        firebaseAnalytics.logEvent("open_study_set") {
            param("study_set_id", id)
            param("study_set_title", _uiState.value.title)
        }
    }

    private fun initData(isRefresh: Boolean = false) {
        getStudySetDetail(isRefresh = isRefresh)
        getStudyTimeByStudySetId()
    }

    fun onEvent(event: StudySetDetailUiAction) {
        when (event) {
            is StudySetDetailUiAction.Refresh -> {
                job?.cancel()
                job = viewModelScope.launch {
                    initData(isRefresh = true)
                }
            }

            is StudySetDetailUiAction.OnIdOfFlashCardSelectedChanged -> {
                _uiState.update { it.copy(idOfFlashCardSelected = event.id) }
            }

            is StudySetDetailUiAction.OnDeleteFlashCardClicked -> {
                deleteFlashCard()
            }

            is StudySetDetailUiAction.OnStarFlashCardClicked -> {
                toggleStarredFlashCard(event.id, event.isStarred)
            }

            is StudySetDetailUiAction.OnEditStudySetClicked -> {
                _uiEvent.trySend(NavigateToEditStudySet)
            }

            is StudySetDetailUiAction.OnEditFlashCardClicked -> {
                _uiEvent.trySend(NavigateToEditFlashCard)
            }

            is StudySetDetailUiAction.OnDeleteStudySetClicked -> {
                deleteStudySet()
            }

            is StudySetDetailUiAction.OnResetProgressClicked -> {
                resetProgress(event.id)
            }

            is StudySetDetailUiAction.OnMakeCopyClicked -> {
                makeCopyStudySet()
            }

            is StudySetDetailUiAction.NavigateToLearn -> {
                firebaseAnalytics.logEvent("navigate_to_learn") {
                    param("study_set_id", _uiState.value.id)
                    param("study_set_title", _uiState.value.title)
                    param("learn_mode", event.learnMode.mode)
                }
                when (event.learnMode) {
                    LearnMode.FLIP -> {
                        _uiEvent.trySend(OnNavigateToFlipFlashcard(event.isGetAll))
                    }

                    LearnMode.QUIZ -> {
                        _uiEvent.trySend(OnNavigateToQuiz(event.isGetAll))
                    }

                    LearnMode.TRUE_FALSE -> {
                        _uiEvent.trySend(OnNavigateToTrueFalse(event.isGetAll))
                    }

                    LearnMode.WRITE -> {
                        _uiEvent.trySend(OnNavigateToWrite(event.isGetAll))
                    }

                    else -> {
                        // Do nothing
                    }
                }
            }

            is StudySetDetailUiAction.OnGetSpeech -> {
                getSpeech(
                    event.term,
                    event.definition,
                    event.termVoiceCode,
                    event.definitionVoiceCode,
                    event.onTermSpeakStart,
                    event.onTermSpeakEnd,
                    event.onDefinitionSpeakStart,
                    event.onDefinitionSpeakEnd
                )
            }
        }
    }

    private fun getStudySetDetail(isRefresh: Boolean = false) {
        val id = _uiState.value.id
        val code = _uiState.value.linkShareCode
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            if (token.isEmpty()) {
                _uiEvent.send(UnAuthorized)
                return@launch
            }
            if (code.isNotEmpty()) {
                studySetRepository.getStudySetByCode(token, code).collect { resource ->
                    when (resource) {
                        is Resources.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false
                                )
                            }
                            _uiEvent.send(NotFound)
                        }

                        is Resources.Loading -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = true
                                )
                            }
                        }

                        is Resources.Success -> {
                            val isOwner =
                                appManager.userId.firstOrNull() == resource.data!!.owner.id
                            _uiState.update {
                                it.copy(
                                    title = resource.data.title,
                                    description = resource.data.description ?: "",
                                    color = resource.data.color!!.hexValue.toColor(),
                                    subject = resource.data.subject!!,
                                    flashCardCount = resource.data.flashcardCount,
                                    flashCards = resource.data.flashcards,
                                    isPublic = resource.data.isPublic,
                                    user = resource.data.owner,
                                    createdAt = resource.data.createdAt,
                                    updatedAt = resource.data.updatedAt,
                                    colorModel = resource.data.color,
                                    linkShareCode = resource.data.linkShareCode ?: "",
                                    isLoading = false,
                                    isAIGenerated = resource.data.isAIGenerated == true,
                                    isOwner = isOwner,
                                )
                            }
                            if (!isRefresh && resource.data.id.isNotEmpty()) {
                                saveRecentAccessStudySet(resource.data.id)
                            }
                        }
                    }
                }
            } else {
                studySetRepository.getStudySetById(token, id).collect { resource ->
                    when (resource) {
                        is Resources.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }

                        is Resources.Success -> {
                            val isOwner =
                                appManager.userId.firstOrNull() == resource.data!!.owner.id
                            _uiState.update {
                                it.copy(
                                    title = resource.data.title,
                                    description = resource.data.description ?: "",
                                    color = resource.data.color!!.hexValue.toColor(),
                                    subject = resource.data.subject!!,
                                    flashCardCount = resource.data.flashcardCount,
                                    flashCards = resource.data.flashcards,
                                    isPublic = resource.data.isPublic,
                                    user = resource.data.owner,
                                    createdAt = resource.data.createdAt,
                                    updatedAt = resource.data.updatedAt,
                                    colorModel = resource.data.color,
                                    linkShareCode = resource.data.linkShareCode ?: "",
                                    isLoading = false,
                                    isAIGenerated = resource.data.isAIGenerated == true,
                                    isOwner = isOwner,
                                )
                            }
                            if (!isRefresh) {
                                saveRecentAccessStudySet(id)
                            }
                        }

                        is Resources.Error -> {
                            _uiState.update { it.copy(isLoading = false) }
                        }
                    }
                }
            }
        }
    }

    private fun saveRecentAccessStudySet(studySetId: String) {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            val userId = appManager.userId.firstOrNull() ?: ""
            val saveRecentAccessStudySetRequestModel = SaveRecentAccessStudySetRequestModel(
                userId = userId,
                studySetId = studySetId
            )
            studySetRepository.saveRecentAccessStudySet(
                token,
                saveRecentAccessStudySetRequestModel
            ).collect()
        }
    }

    private fun deleteFlashCard() {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            flashCardRepository.deleteFlashCard(token, _uiState.value.idOfFlashCardSelected)
                .collect { resource ->
                    when (resource) {
                        is Resources.Loading -> {
                            Timber.d("Loading")
                        }

                        is Resources.Success -> {
                            _uiEvent.send(FlashCardDeleted)
                        }

                        is Resources.Error -> {
                            Timber.d(resource.message)
                        }
                    }
                }
        }
    }

    private fun toggleStarredFlashCard(id: String, isStarred: Boolean) {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            flashCardRepository.toggleStarredFlashCard(
                token,
                id,
                isStarred
            ).collect { resource ->
                when (resource) {
                    is Resources.Loading -> {
                        Timber.d("Loading")
                    }

                    is Resources.Success -> {
                        Timber.d(resource.data?.message)
                        _uiEvent.send(StudySetDetailUiEvent.FlashCardStarred)
                    }

                    is Resources.Error -> {
                        Timber.d("Error")
                    }
                }
            }
        }
    }

    private fun deleteStudySet() {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            studySetRepository.deleteStudySet(token, _uiState.value.id)
                .collect { resource ->
                    when (resource) {
                        is Resources.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }

                        is Resources.Success -> {
                            _uiEvent.send(StudySetDeleted)
                        }

                        is Resources.Error -> {
                            Timber.d(resource.message)
                            _uiState.update { it.copy(isLoading = false) }
                        }
                    }
                }
        }
    }

    private fun resetProgress(id: String) {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            studySetRepository.resetProgress(token, id, resetType = ResetType.RESET_ALL.type)
                .collect { resource ->
                    when (resource) {
                        is Resources.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }

                        is Resources.Success -> {
                            _uiState.update { it.copy(isLoading = false) }
                            _uiEvent.send(StudySetProgressReset)
                        }

                        is Resources.Error -> {
                            Timber.d("Error")
                            _uiState.update { it.copy(isLoading = false) }
                        }
                    }
                }
        }
    }

    private fun makeCopyStudySet() {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            val userId = appManager.userId.firstOrNull() ?: ""
            val studySetId = _uiState.value.id
            studySetRepository.makeCopyStudySet(token, studySetId, userId).collect { resource ->
                when (resource) {
                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resources.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEvent.send(
                            StudySetCopied(
                                resource.data?.id ?: ""
                            )
                        )
                    }

                    is Resources.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }

    private fun getStudyTimeByStudySetId() {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            val studySetId = _uiState.value.id

            studyTimeRepository.getStudyTimeByStudySet(token, studySetId).collect { resource ->
                when (resource) {
                    is Resources.Loading -> {
                        Timber.d("Loading")
                    }

                    is Resources.Success -> {
                        _uiState.update { it.copy(studyTime = resource.data) }
                    }

                    is Resources.Error -> {
                        Timber.d("Error")
                    }
                }
            }
        }
    }

    private fun getSpeech(
        term: String,
        definition: String,
        termVoiceCode: String,
        definitionVoiceCode: String,
        onTermSpeakStart: () -> Unit,
        onTermSpeakEnd: () -> Unit,
        onDefinitionSpeakStart: () -> Unit,
        onDefinitionSpeakEnd: () -> Unit
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""

            val termByteArray = fetchSpeech(token, term, termVoiceCode)
            if (termByteArray != null) {
                onTermSpeakStart()
                playAudioAndWait(termByteArray)
                onTermSpeakEnd()
            } else {
                Timber.d("Không lấy được dữ liệu âm thanh cho term")
            }

            val definitionByteArray = fetchSpeech(token, definition, definitionVoiceCode)
            if (definitionByteArray != null) {
                onDefinitionSpeakStart()
                playAudioAndWait(definitionByteArray)
                onDefinitionSpeakEnd()
            } else {
                Timber.d("Không lấy được dữ liệu âm thanh cho definition")
            }
        }
    }

    private suspend fun fetchSpeech(token: String, input: String, voiceCode: String): ByteArray? {
        var byteArray: ByteArray? = null
        flashCardRepository.getSpeech(token = token, input = input, voiceCode = voiceCode)
            .collect { resource ->
                when (resource) {
                    is Resources.Loading -> {
                        Timber.d("Loading")
                    }

                    is Resources.Success -> {
                        byteArray = resource.data?.toByteArray()
                    }

                    is Resources.Error -> {
                        Timber.d("Error")
                    }
                }
            }
        return byteArray
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun playAudioAndWait(audioData: ByteArray) =
        suspendCancellableCoroutine<Unit> { cont ->
            val audioFile = AudioExtension.convertByteArrayToAudio(
                getApplication<Application>().applicationContext,
                audioData,
                "audio.wav"
            ) ?: return@suspendCancellableCoroutine
            AudioExtension.playAudio(
                context = getApplication<Application>().applicationContext,
                audioFile = audioFile,
                onCompletion = {
                    cont.resume(Unit) {}
                }
            )
        }
}