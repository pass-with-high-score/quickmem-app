package com.pwhs.quickmem.presentation.app.study_set.studies.write

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.LearnFrom
import com.pwhs.quickmem.core.data.enums.LearnMode
import com.pwhs.quickmem.core.data.enums.ResetType
import com.pwhs.quickmem.core.data.enums.WriteStatus
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.domain.model.flashcard.FlashCardResponseModel
import com.pwhs.quickmem.domain.model.study_set.CreateWriteHintAIRequestModel
import com.pwhs.quickmem.domain.model.study_time.CreateStudyTimeModel
import com.pwhs.quickmem.domain.model.subject.SubjectModel
import com.pwhs.quickmem.domain.repository.FlashCardRepository
import com.pwhs.quickmem.domain.repository.StudySetRepository
import com.pwhs.quickmem.domain.repository.StudyTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LearnByWriteViewModel @Inject constructor(
    private val flashCardRepository: FlashCardRepository,
    private val studySetRepository: StudySetRepository,
    private val studyTimeRepository: StudyTimeRepository,
    private val tokenManager: TokenManager,
    private val appManager: AppManager,
    savedStateHandle: SavedStateHandle,
    application: Application,
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LearnByTrueFalseUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<LearnByWriteUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val studySetId: String = savedStateHandle.get<String>("studySetId") ?: ""
        val isGetAll: Boolean = savedStateHandle.get<Boolean>("isGetAll") == true
        val studySetTitle: String = savedStateHandle.get<String>("studySetTitle") ?: ""
        val studySetDescription: String = savedStateHandle.get<String>("studySetDescription") ?: ""
        val studySetColorId: Int = savedStateHandle.get<Int>("studySetColorId") ?: 1
        val studySetSubjectId: Int = savedStateHandle.get<Int>("studySetSubjectId") ?: 1
        val folderId: String = savedStateHandle.get<String>("folderId") ?: ""
        val learnFrom: LearnFrom =
            savedStateHandle.get<LearnFrom>("learnFrom") ?: LearnFrom.STUDY_SET
        _uiState.update { state ->
            state.copy(
                folderId = folderId,
                learnFrom = learnFrom,
                isGetAll = isGetAll,
                studySetId = studySetId,
                studySetTitle = studySetTitle,
                studySetDescription = studySetDescription,
                studySetColor = ColorModel.defaultColors.first { it.id == studySetColorId },
                studySetSubject = SubjectModel.defaultSubjects.first { it.id == studySetSubjectId },
                startTime = System.currentTimeMillis()
            )
        }
        viewModelScope.launch {
            val isPlaySound = appManager.isPlaySound.firstOrNull() == true
            _uiState.update { it.copy(isPlaySound = isPlaySound) }
        }

        getFlashCard()
    }

    fun onEvent(event: LearnByWriteUiAction) {
        when (event) {
            is LearnByWriteUiAction.LoadNextFlashCard -> {
                loadNextFlashCard(
                    _uiState.value.nextFlashCard,
                )
            }

            is LearnByWriteUiAction.OnAnswer -> {
                submitCorrectAnswer(
                    flashCardId = event.flashcardId,
                    writeStatus = event.writeStatus,
                    userAnswer = event.userAnswer
                )
            }

            is LearnByWriteUiAction.ContinueLearnWrongAnswer -> {
                _uiState.update {
                    it.copy(
                        currentCardIndex = 0,
                        learningTime = 0,
                        startTime = System.currentTimeMillis(),
                        isEndOfList = false,
                        wrongAnswerCount = 0,
                        listWrongAnswer = emptyList()
                    )
                }
                getFlashCard()
            }

            is LearnByWriteUiAction.RestartLearn -> {
                onRestart()
            }

            is LearnByWriteUiAction.OnBackClicked -> {
                _uiState.update {
                    it.copy(learningTime = System.currentTimeMillis() - it.startTime)
                }
                if (!_uiState.value.isEndOfList) {
                    sendCompletedStudyTime()
                }
                _uiEvent.trySend(LearnByWriteUiEvent.Back)
            }

            is LearnByWriteUiAction.OnCreateHintByAI -> {
                createHintByAI()
            }

            LearnByWriteUiAction.OnSwapCard -> {
                _uiState.update {
                    it.copy(
                        isSwapCard = !it.isSwapCard,
                        currentCardIndex = 0,
                        learningTime = 0,
                        wrongAnswerCount = 0,
                        listWrongAnswer = emptyList()
                    )
                }
                getFlashCard()
            }
        }
    }

    private fun getFlashCard() {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            val studySetId = _uiState.value.studySetId
            val folderId = _uiState.value.folderId
            val learnFrom = _uiState.value.learnFrom
            val isGetAll = _uiState.value.isGetAll
            val isSwapCard = _uiState.value.isSwapCard
            val isRandomCard = _uiState.value.isRandomCard
            when (learnFrom) {
                LearnFrom.STUDY_SET -> {
                    flashCardRepository.getFlashCardsByStudySetId(
                        token = token,
                        studySetId = studySetId,
                        learnMode = LearnMode.WRITE,
                        isGetAll = isGetAll,
                        isSwapped = isSwapCard,
                        isRandom = isRandomCard
                    ).collect { resource ->
                        when (resource) {
                            is Resources.Error -> {
                                _uiState.update { it.copy(isLoading = false) }
                            }

                            is Resources.Loading -> {
                                _uiState.update { it.copy(isLoading = true) }
                            }

                            is Resources.Success -> {
                                if (resource.data.isNullOrEmpty()) {
                                    _uiState.update {
                                        it.copy(
                                            learningTime = 0,
                                            isLoading = false,
                                            isEndOfList = true
                                        )
                                    }
                                    playCompleteSound()
                                    _uiEvent.send(LearnByWriteUiEvent.Finished)
                                    return@collect
                                }

                                val flashCards = resource.data
                                val currentCard = flashCards.firstOrNull()

                                _uiState.update {
                                    it.copy(
                                        flashCardList = flashCards,
                                        writeQuestion = generateQuestion(currentCard),
                                        isLoading = false,
                                        currentFlashCard = currentCard,
                                        nextFlashCard = flashCards.getOrNull(1)
                                    )
                                }
                            }
                        }

                    }
                }

                LearnFrom.FOLDER -> {
                    flashCardRepository.getFlashCardsByFolderId(
                        token = token,
                        folderId = folderId,
                        learnMode = LearnMode.WRITE,
                        isGetAll = isGetAll,
                        isSwapped = isSwapCard,
                        isRandom = isRandomCard
                    ).collect { resource ->
                        when (resource) {
                            is Resources.Error -> {
                                _uiState.update { it.copy(isLoading = false) }
                            }

                            is Resources.Loading -> {
                                _uiState.update { it.copy(isLoading = true) }
                            }

                            is Resources.Success -> {
                                if (resource.data.isNullOrEmpty()) {
                                    _uiState.update {
                                        it.copy(
                                            learningTime = 0,
                                            isLoading = false,
                                            isEndOfList = true
                                        )
                                    }
                                    playCompleteSound()
                                    _uiEvent.send(LearnByWriteUiEvent.Finished)
                                    return@collect
                                }

                                val flashCards = resource.data
                                val currentCard = flashCards.firstOrNull()

                                _uiState.update {
                                    it.copy(
                                        flashCardList = flashCards,
                                        writeQuestion = generateQuestion(currentCard),
                                        isLoading = false,
                                        currentFlashCard = currentCard,
                                        nextFlashCard = flashCards.getOrNull(1)
                                    )
                                }
                            }
                        }
                    }
                }

                LearnFrom.CLASS -> {
                    // TODO: get flash card by class
                }
            }
        }
    }

    private fun submitCorrectAnswer(
        flashCardId: String,
        writeStatus: WriteStatus,
        userAnswer: String,
    ) {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            val isPlaySound = _uiState.value.isPlaySound
            if (isPlaySound) {
                when (writeStatus) {
                    WriteStatus.CORRECT -> playCorrectSound()
                    WriteStatus.WRONG -> playIncorrectSound()
                    else -> {}
                }
            }
            try {
                flashCardRepository.updateWriteStatus(
                    token = token,
                    id = flashCardId,
                    writeStatus = writeStatus.status
                ).collect { resource ->
                    when (resource) {
                        is Resources.Error -> {
                            Timber.e(resource.message)
                        }

                        is Resources.Loading -> {
                            Timber.d("Loading")
                        }

                        is Resources.Success -> {

                            // mark the current card as answered
                            val flashCardList = _uiState.value.flashCardList.map {
                                if (it.id == flashCardId) {
                                    it.copy(isAnswered = true)
                                } else {
                                    it
                                }
                            }

                            // get the next flash card (one that is not answered)
                            val nextFlashCard = flashCardList.firstOrNull { !it.isAnswered }

                            _uiState.update {
                                it.copy(
                                    flashCardList = flashCardList,
                                    nextFlashCard = nextFlashCard,
                                    wrongAnswerCount = if (writeStatus == WriteStatus.WRONG || writeStatus == WriteStatus.SKIPPED) it.wrongAnswerCount + 1 else it.wrongAnswerCount,
                                    listWrongAnswer = if (writeStatus == WriteStatus.WRONG || writeStatus == WriteStatus.SKIPPED) it.listWrongAnswer + WriteQuestion(
                                        id = flashCardId,
                                        term = it.currentFlashCard?.term ?: "",
                                        definition = it.currentFlashCard?.definition ?: "",
                                        definitionImageUrl = it.currentFlashCard?.definitionImageURL
                                            ?: "",
                                        userAnswer = userAnswer,
                                        hint = it.currentFlashCard?.hint ?: ""
                                    ) else it.listWrongAnswer
                                )
                            }
                        }
                    }

                }
                loadNextFlashCard(_uiState.value.nextFlashCard)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }


    private fun loadNextFlashCard(
        nextFlashCard: FlashCardResponseModel?,
    ) {
        if (nextFlashCard != null && _uiState.value.currentCardIndex < _uiState.value.flashCardList.size - 1) {
            _uiState.update {
                it.copy(
                    writeQuestion = generateQuestion(nextFlashCard),
                    currentCardIndex = it.currentCardIndex + 1,
                    currentFlashCard = nextFlashCard,
                    nextFlashCard = it.flashCardList.getOrNull(it.currentCardIndex + 2)
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    learningTime = System.currentTimeMillis() - it.startTime,
                    isEndOfList = true
                )
            }
            playCompleteSound()
            sendCompletedStudyTime()
            _uiEvent.trySend(LearnByWriteUiEvent.Finished)
        }
    }

    private fun generateQuestion(
        currentCard: FlashCardResponseModel?,
    ): WriteQuestion? {
        if (currentCard == null) return null

        return WriteQuestion(
            id = currentCard.id,
            term = currentCard.term,
            definition = currentCard.definition,
            hint = currentCard.hint ?: "",
            definitionImageUrl = currentCard.definitionImageURL ?: "",
            userAnswer = ""
        )
    }

    private fun sendCompletedStudyTime() {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            val userId = appManager.userId.firstOrNull() ?: ""
            val createStudyTimeModel = CreateStudyTimeModel(
                learnMode = LearnMode.WRITE.mode,
                studySetId = _uiState.value.studySetId,
                timeSpent = _uiState.value.learningTime.toInt(),
                userId = userId
            )
            studyTimeRepository.createStudyTime(token, createStudyTimeModel)
                .collect()
        }
    }

    private fun onRestart() {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            val studySetId = _uiState.value.studySetId
            studySetRepository.resetProgress(
                token,
                studySetId,
                ResetType.WRITE_STATUS.type
            ).collect { resource ->
                when (resource) {
                    is Resources.Error -> {
                        Timber.e(resource.message)
                        _uiState.update { it.copy(isLoading = false) }
                    }

                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resources.Success -> {
                        // reset all state
                        _uiState.update {
                            it.copy(
                                currentCardIndex = 0,
                                learningTime = 0,
                                startTime = System.currentTimeMillis(),
                                isEndOfList = false,
                                wrongAnswerCount = 0,
                                listWrongAnswer = emptyList()
                            )
                        }
                        getFlashCard()
                    }
                }
            }
        }
    }

    private fun playCompleteSound() {
        val isPlaySound = _uiState.value.isPlaySound
        if (isPlaySound) {
            val mediaPlayer = MediaPlayer.create(getApplication(), R.raw.audio_study_complete)
            mediaPlayer.start()
        }
    }

    private fun playCorrectSound() {
        val mediaPlayer = MediaPlayer.create(getApplication(), R.raw.audio_correct)
        mediaPlayer.start()
    }

    private fun playIncorrectSound() {
        val mediaPlayer = MediaPlayer.create(getApplication(), R.raw.audio_wrong)
        mediaPlayer.start()
    }

    private fun createHintByAI(
    ) {
        viewModelScope.launch {
            val flashcardId: String = _uiState.value.writeQuestion?.id ?: ""
            val question: String = _uiState.value.writeQuestion?.term ?: ""
            val answer: String = _uiState.value.writeQuestion?.definition ?: ""
            val studySetTitle: String = _uiState.value.studySetTitle
            val studySetDescription: String = _uiState.value.studySetDescription
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            val createFlashCardModel = CreateWriteHintAIRequestModel(
                flashcardId = flashcardId,
                studySetTitle = studySetTitle.ifEmpty { "No title" },
                studySetDescription = studySetDescription.ifEmpty { "No description" },
                question = question,
                answer = answer
            )
            studySetRepository.createWriteHintAI(
                token = token,
                createWriteHintAIRequestModel = createFlashCardModel
            ).collect { resource ->
                when (resource) {
                    is Resources.Error -> {
                        Timber.e(resource.message)
                        _uiState.update {
                            it.copy(
                                isGenerateHint = false,
                                writeQuestion = it.writeQuestion?.copy(aiHint = resource.message)
                            )
                        }
                    }

                    is Resources.Loading -> {
                        _uiState.update { it.copy(isGenerateHint = true) }
                    }

                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(
                                isGenerateHint = false,
                                writeQuestion = it.writeQuestion?.copy(aiHint = resource.data?.aiHint)
                            )
                        }
                    }
                }
            }
        }
    }
}