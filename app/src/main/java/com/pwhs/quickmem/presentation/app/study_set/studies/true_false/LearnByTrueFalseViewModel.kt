package com.pwhs.quickmem.presentation.app.study_set.studies.true_false

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.LearnFrom
import com.pwhs.quickmem.core.data.enums.LearnMode
import com.pwhs.quickmem.core.data.enums.ResetType
import com.pwhs.quickmem.core.data.enums.TrueFalseStatus
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.domain.model.flashcard.FlashCardResponseModel
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
class LearnByTrueFalseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val flashCardRepository: FlashCardRepository,
    private val studySetRepository: StudySetRepository,
    private val studyTimeRepository: StudyTimeRepository,
    private val tokenManager: TokenManager,
    private val appManager: AppManager,
    application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LearnByTrueFalseUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<LearnByTrueFalseUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val studySetId = savedStateHandle.get<String>("studySetId") ?: ""
        val isGetAll = savedStateHandle.get<Boolean>("isGetAll") == true
        val studySetTitle = savedStateHandle.get<String>("studySetTitle") ?: ""
        val studySetDescription = savedStateHandle.get<String>("studySetDescription") ?: ""
        val studySetColorId = savedStateHandle.get<Int>("studySetColorId") ?: 1
        val studySetSubjectId = savedStateHandle.get<Int>("studySetSubjectId") ?: 1
        val folderId = savedStateHandle.get<String>("folderId") ?: ""
        val learnFrom = savedStateHandle.get<LearnFrom>("learnFrom") ?: LearnFrom.STUDY_SET
        _uiState.update { state ->
            state.copy(
                folderId = folderId,
                learnFrom = learnFrom,
                studySetId = studySetId,
                isGetAll = isGetAll,
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

    fun onEvent(event: LearnByTrueFalseUiAction) {
        when (event) {
            is LearnByTrueFalseUiAction.LoadNextFlashCard -> {
                loadNextFlashCard(
                    _uiState.value.nextFlashCard,
                )
            }

            is LearnByTrueFalseUiAction.OnAnswer -> {
                submitCorrectAnswer(
                    event.flashcardId,
                    event.isCorrect
                )
            }

            is LearnByTrueFalseUiAction.ContinueLearnWrongAnswer -> {
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

            is LearnByTrueFalseUiAction.RestartLearn -> {
                onRestart()
            }

            is LearnByTrueFalseUiAction.OnBackClicked -> {
                _uiState.update {
                    it.copy(learningTime = System.currentTimeMillis() - it.startTime)
                }
                if (!_uiState.value.isEndOfList) {
                    sendCompletedStudyTime()
                }
                _uiEvent.trySend(LearnByTrueFalseUiEvent.Back)
            }

            is LearnByTrueFalseUiAction.OnChangeIsPlaySound -> {
                _uiState.update {
                    it.copy(isPlaySound = event.isPlaySound)
                }
                viewModelScope.launch {
                    appManager.saveIsPlaySound(event.isPlaySound)
                }
            }

            is LearnByTrueFalseUiAction.OnSwapCard -> {
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
                        learnMode = LearnMode.TRUE_FALSE,
                        isGetAll = isGetAll,
                        isSwapped = isSwapCard,
                        isRandom = isRandomCard,
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
                                    _uiEvent.send(LearnByTrueFalseUiEvent.Finished)
                                    return@collect
                                }

                                val flashCards = resource.data
                                val currentCard = flashCards.firstOrNull()
                                val question = generateQuestion(flashCards, currentCard)

                                _uiState.update { it: LearnByTrueFalseUiState ->
                                    it.copy(
                                        flashCardList = flashCards,
                                        randomQuestion = question,
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
                        learnMode = LearnMode.TRUE_FALSE,
                        isGetAll = isGetAll,
                        isSwapped = isSwapCard,
                        isRandom = isRandomCard,
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
                                    _uiEvent.send(LearnByTrueFalseUiEvent.Finished)
                                    return@collect
                                }

                                val flashCards = resource.data
                                val currentCard = flashCards.firstOrNull()
                                val question = generateQuestion(flashCards, currentCard)

                                _uiState.update {
                                    it.copy(
                                        flashCardList = flashCards,
                                        randomQuestion = question,
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
                    // TODO: get flash cards by class
                }
            }
        }
    }

    private fun submitCorrectAnswer(
        flashCardId: String,
        isCorrect: Boolean,
    ) {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            val isPlaySound = _uiState.value.isPlaySound
            if (isPlaySound) {
                when (isCorrect) {
                    true -> playCorrectSound()
                    false -> playIncorrectSound()
                }
            }
            try {
                flashCardRepository.updateTrueFalseStatus(
                    token = token,
                    id = flashCardId,
                    trueFalseStatus = when (isCorrect) {
                        true -> TrueFalseStatus.CORRECT.status
                        false -> TrueFalseStatus.WRONG.status
                    }
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

                            // update listWrongAnswer based on the correctness of the answer
                            val updatedListWrongAnswer = if (isCorrect) {
                                _uiState.value.listWrongAnswer.filter { it.id != flashCardId }
                            } else {
                                _uiState.value.listWrongAnswer + _uiState.value.randomQuestion!!
                            }

                            _uiState.update {
                                it.copy(
                                    flashCardList = flashCardList,
                                    nextFlashCard = nextFlashCard,
                                    wrongAnswerCount = if (!isCorrect) it.wrongAnswerCount + 1 else it.wrongAnswerCount,
                                    listWrongAnswer = updatedListWrongAnswer
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

    private fun generateQuestion(
        flashCards: List<FlashCardResponseModel>,
        currentCard: FlashCardResponseModel?
    ): TrueFalseQuestion? {
        if (currentCard == null) return null

        val random = (0..1).random() == 1
        val filteredFlashCards = flashCards.filter { it.id != currentCard.id }

        if (filteredFlashCards.isEmpty()) {
            return TrueFalseQuestion(
                id = currentCard.id,
                term = currentCard.term,
                termImageUrl = currentCard.termImageURL ?: "",
                definition = currentCard.definition,
                definitionImageUrl = currentCard.definitionImageURL ?: "",
                isRandom = false,
                originalDefinition = currentCard.definition,
                originalDefinitionImageUrl = currentCard.definitionImageURL ?: ""
            )
        }

        val randomFlashCard = filteredFlashCards.random()

        return TrueFalseQuestion(
            id = currentCard.id,
            term = currentCard.term,
            termImageUrl = currentCard.termImageURL ?: "",
            definition = if (random) randomFlashCard.definition else currentCard.definition,
            definitionImageUrl = if (random) randomFlashCard.definitionImageURL
                ?: "" else currentCard.definitionImageURL ?: "",
            isRandom = random,
            originalDefinition = currentCard.definition,
            originalDefinitionImageUrl = currentCard.definitionImageURL ?: ""
        )
    }

    private fun loadNextFlashCard(
        nextFlashCard: FlashCardResponseModel?,
    ) {
        if (nextFlashCard != null && _uiState.value.currentCardIndex < _uiState.value.flashCardList.size - 1) {
            val randomAnswers = generateQuestion(_uiState.value.flashCardList, nextFlashCard)
            _uiState.update {
                it.copy(
                    currentCardIndex = it.currentCardIndex + 1,
                    randomQuestion = randomAnswers,
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
            _uiEvent.trySend(LearnByTrueFalseUiEvent.Finished)
        }
    }

    private fun sendCompletedStudyTime() {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            val userId = appManager.userId.firstOrNull() ?: ""
            val createStudyTimeModel = CreateStudyTimeModel(
                learnMode = LearnMode.TRUE_FALSE.mode,
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
                ResetType.TRUE_FALSE_STATUS.type
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
}