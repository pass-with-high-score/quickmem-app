package com.pwhs.quickmem.presentation.app.study_set.studies.quiz

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.LearnFrom
import com.pwhs.quickmem.core.data.enums.LearnMode
import com.pwhs.quickmem.core.data.enums.QuizStatus
import com.pwhs.quickmem.core.data.enums.ResetType
import com.pwhs.quickmem.core.data.states.RandomAnswer
import com.pwhs.quickmem.core.data.states.WrongAnswer
import com.pwhs.quickmem.core.datastore.AppManager
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
class LearnByQuizViewModel @Inject constructor(
    private val flashCardRepository: FlashCardRepository,
    private val studySetRepository: StudySetRepository,
    private val studyTimeRepository: StudyTimeRepository,
    private val appManager: AppManager,
    savedStateHandle: SavedStateHandle,
    application: Application,
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(LearnFlashCardUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<LearnByQuizUiEvent>()
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
                studySetId = studySetId,
                folderId = folderId,
                learnFrom = learnFrom,
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
            _uiState.update {
                it.copy(isPlaySound = isPlaySound)
            }
        }

        getFlashCard()
    }

    fun onEvent(event: LearnByQuizUiAction) {
        when (event) {
            is LearnByQuizUiAction.LoadNextFlashCard -> {
                loadNextFlashCard(
                    _uiState.value.nextFlashCard,
                )
            }

            is LearnByQuizUiAction.SubmitCorrectAnswer -> submitCorrectAnswer(
                event.flashCardId,
                event.quizStatus,
                event.userAnswer
            )

            is LearnByQuizUiAction.ContinueLearnWrongAnswer -> {
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

            is LearnByQuizUiAction.RestartLearn -> {
                onRestart()
            }

            is LearnByQuizUiAction.OnBackClicked -> {
                _uiState.update {
                    it.copy(learningTime = System.currentTimeMillis() - it.startTime)
                }
                if (!_uiState.value.isEndOfList) {
                    sendCompletedStudyTime()
                }
                _uiEvent.trySend(LearnByQuizUiEvent.Back)
            }

            is LearnByQuizUiAction.OnChangeIsPlaySound -> {
                _uiState.update {
                    it.copy(isPlaySound = event.isPlaySound)
                }
                viewModelScope.launch {
                    appManager.saveIsPlaySound(event.isPlaySound)
                }
            }

            is LearnByQuizUiAction.OnSwapCard -> {
                _uiState.update {
                    it.copy(
                        isSwapCard = !it.isSwapCard,
                        currentCardIndex = 0,
                        learningTime = 0,
                        isEndOfList = false,
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
            val studySetId = _uiState.value.studySetId
            val folderId = _uiState.value.folderId
            val learnFrom = _uiState.value.learnFrom
            val isGetAll = _uiState.value.isGetAll
            val isSwapCard = _uiState.value.isSwapCard
            val isRandomCard = _uiState.value.isRandomCard
            when (learnFrom) {
                LearnFrom.STUDY_SET -> {
                    flashCardRepository.getFlashCardsByStudySetId(
                        studySetId = studySetId,
                        learnMode = LearnMode.QUIZ,
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
                                    return@collect
                                }

                                val flashCards = resource.data
                                val currentCard = flashCards.firstOrNull()
                                val randomAnswers = generateRandomAnswers(
                                    flashCards = flashCards,
                                    currentCard = currentCard,
                                )

                                _uiState.update {
                                    it.copy(
                                        flashCardList = flashCards,
                                        randomAnswers = randomAnswers,
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
                        folderId = folderId,
                        learnMode = LearnMode.QUIZ,
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
                                    return@collect
                                }

                                val flashCards = resource.data
                                val currentCard = flashCards.firstOrNull()
                                val randomAnswers = generateRandomAnswers(
                                    flashCards = flashCards,
                                    currentCard = currentCard,
                                )

                                _uiState.update {
                                    it.copy(
                                        flashCardList = flashCards,
                                        randomAnswers = randomAnswers,
                                        isLoading = false,
                                        currentFlashCard = currentCard,
                                        nextFlashCard = flashCards.getOrNull(1)
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }


    private fun submitCorrectAnswer(
        flashCardId: String,
        quizStatus: QuizStatus,
        userAnswer: String = "",
    ) {
        viewModelScope.launch {
            val isPlaySound = _uiState.value.isPlaySound
            if (isPlaySound) {
                when (quizStatus) {
                    QuizStatus.CORRECT -> playCorrectSound()
                    QuizStatus.WRONG -> playIncorrectSound()
                    else -> {
                    }
                }
            }
            try {
                flashCardRepository.updateQuizStatus(
                    id = flashCardId,
                    quizStatus = quizStatus.status
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
                                    wrongAnswerCount = if (quizStatus == QuizStatus.WRONG || quizStatus == QuizStatus.SKIPPED) it.wrongAnswerCount + 1 else it.wrongAnswerCount,
                                    listWrongAnswer = if (quizStatus == QuizStatus.WRONG) it.listWrongAnswer + WrongAnswer(
                                        flashCard = it.currentFlashCard!!,
                                        userAnswer = userAnswer
                                    ) else it.listWrongAnswer
                                )
                            }
                        }
                    }

                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun generateRandomAnswers(
        flashCards: List<FlashCardResponseModel>,
        currentCard: FlashCardResponseModel?,
        lastAnswer: String? = null,
    ): List<RandomAnswer> {
        if (currentCard == null) return emptyList()

        val answers = flashCards
            .filter { it.id != currentCard.id && it.definition != lastAnswer }
            .distinctBy { it.definition }
            .shuffled()
            .take(3)
            .map {
                RandomAnswer(
                    answer = it.definition,
                    isCorrect = false,
                    answerImage = it.definitionImageURL ?: ""
                )
            }
            .toMutableList()

        answers.add(
            RandomAnswer(
                answer = currentCard.definition,
                isCorrect = true,
                answerImage = currentCard.definitionImageURL ?: ""
            )
        )
        return answers.shuffled()
    }

    private fun loadNextFlashCard(
        nextFlashCard: FlashCardResponseModel?,
    ) {
        if (nextFlashCard != null && _uiState.value.currentCardIndex < _uiState.value.flashCardList.size - 1) {
            val randomAnswers = generateRandomAnswers(
                flashCards = _uiState.value.flashCardList,
                currentCard = nextFlashCard,
                lastAnswer = _uiState.value.currentFlashCard?.definition
            )
            _uiState.update {
                it.copy(
                    currentCardIndex = it.currentCardIndex + 1,
                    randomAnswers = randomAnswers,
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
            _uiEvent.trySend(LearnByQuizUiEvent.Finished)
        }
    }

    private fun sendCompletedStudyTime() {
        viewModelScope.launch {
            val createStudyTimeModel = CreateStudyTimeModel(
                learnMode = LearnMode.QUIZ.mode,
                studySetId = _uiState.value.studySetId,
                timeSpent = _uiState.value.learningTime.toInt(),
            )
            studyTimeRepository.createStudyTime(createStudyTimeModel = createStudyTimeModel)
                .collect()
        }
    }

    private fun onRestart() {
        viewModelScope.launch {
            val studySetId = _uiState.value.studySetId
            studySetRepository.resetProgress(
                studySetId = studySetId,
                resetType = ResetType.QUIZ_STATUS.type
            ).collect { resource ->
                when (resource) {
                    is Resources.Error -> {
                        Timber.e(resource.message)
                        _uiState.update { it.copy(isLoading = false) }
                    }

                    is Resources.Loading -> {
                        Timber.d("Loading")
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