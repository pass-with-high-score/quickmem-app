package com.pwhs.quickmem.presentation.app.study_set.studies.flip

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.FlipCardStatus
import com.pwhs.quickmem.core.data.enums.LearnFrom
import com.pwhs.quickmem.core.data.enums.LearnMode
import com.pwhs.quickmem.core.data.enums.ResetType
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.domain.model.study_time.CreateStudyTimeModel
import com.pwhs.quickmem.domain.model.subject.SubjectModel
import com.pwhs.quickmem.domain.repository.FlashCardRepository
import com.pwhs.quickmem.domain.repository.FolderRepository
import com.pwhs.quickmem.domain.repository.StudySetRepository
import com.pwhs.quickmem.domain.repository.StudyTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlipFlashCardViewModel @Inject constructor(
    private val flashCardRepository: FlashCardRepository,
    private val studySetRepository: StudySetRepository,
    private val studyTimeRepository: StudyTimeRepository,
    private val folderRepository: FolderRepository,
    private val appManager: AppManager,
    savedStateHandle: SavedStateHandle,
    application: Application,
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(FlipFlashCardUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<FlipFlashCardUiEvent>()
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
                learnFrom = learnFrom,
                isGetAll = isGetAll,
                folderId = folderId,
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
            _uiState.update {
                it.copy(isPlaySound = isPlaySound)
            }
        }

        getFlashCard()
    }

    fun onEvent(event: FlipFlashCardUiAction) {
        when (event) {
            is FlipFlashCardUiAction.OnSwipe -> {
                // remove the flash card from the list
                val flashCardList = _uiState.value.flashCardList.toMutableList()
                val index = flashCardList.indexOfFirst { it.id == event.id }
                if (index != -1) {
                    flashCardList.removeAt(index)
                    _uiState.update { it.copy(flashCardList = flashCardList) }
                }
            }

            is FlipFlashCardUiAction.OnUpdateCardIndex -> {
                _uiState.update {
                    it.copy(
                        currentCardIndex = it.currentCardIndex + 1,
                    )
                }
                if (event.index == _uiState.value.flashCardList.size - 1) {
                    _uiState.update {
                        it.copy(
                            isEndOfList = true,
                            learningTime = System.currentTimeMillis() - it.startTime
                        )
                    }
                    playCompleteSound()
                    sendCompletedStudyTime()
                    _uiEvent.trySend(FlipFlashCardUiEvent.Finished)
                    return
                }
            }

            is FlipFlashCardUiAction.OnSwipeLeft -> {
                _uiState.update { it.copy(isSwipingLeft = event.isSwipingLeft) }
            }

            is FlipFlashCardUiAction.OnSwipeRight -> {
                _uiState.update { it.copy(isSwipingRight = event.isSwipingRight) }
            }

            is FlipFlashCardUiAction.OnUpdateCountKnown -> {
                if (event.isIncrease) {
                    _uiState.update {
                        it.copy(
                            countKnown = it.countKnown + 1,
                            isSwipingRight = false
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            countKnown = it.countKnown - 1,
                            isSwipingRight = false
                        )
                    }
                }
                updateFlashCardFlipStatus(
                    isRight = true,
                    event.flashCardId
                )
            }

            is FlipFlashCardUiAction.OnUpdateCountStillLearning -> {
                if (event.isIncrease) {
                    _uiState.update {
                        it.copy(
                            countStillLearning = it.countStillLearning + 1,
                            isSwipingLeft = false
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            countStillLearning = it.countStillLearning - 1,
                            isSwipingLeft = false,
                        )
                    }
                }
                updateFlashCardFlipStatus(
                    isRight = false,
                    event.flashCardId
                )
            }

            is FlipFlashCardUiAction.OnRestartClicked -> {
                restartStudySet()
            }

            is FlipFlashCardUiAction.OnContinueLearningClicked -> {
                _uiState.update {
                    it.copy(
                        isEndOfList = false,
                        countKnown = 0,
                        countStillLearning = 0,
                        currentCardIndex = 0,
                        startTime = System.currentTimeMillis(),
                        flashCardList = emptyList()
                    )
                }

                getFlashCard()
            }

            is FlipFlashCardUiAction.OnBackClicked -> {
                _uiState.update {
                    it.copy(
                        learningTime = System.currentTimeMillis() - it.startTime
                    )
                }
                if (!_uiState.value.isEndOfList) {
                    sendCompletedStudyTime()
                }
                _uiEvent.trySend(FlipFlashCardUiEvent.Back)
            }

            is FlipFlashCardUiAction.OnChangeIsPlaySound -> {
                _uiState.update {
                    it.copy(isPlaySound = event.isPlaySound)
                }
                viewModelScope.launch {
                    appManager.saveIsPlaySound(event.isPlaySound)
                }
            }

            is FlipFlashCardUiAction.OnSwapCard -> {
                // reset other status
                _uiState.update {
                    it.copy(
                        isSwapCard = !it.isSwapCard,
                        isRandomCard = false,
                        countKnown = 0,
                        countStillLearning = 0,
                        currentCardIndex = 0,
                        studySetCardCount = 0,
                        flashCardList = emptyList()
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
                        learnMode = LearnMode.FLIP,
                        isGetAll = isGetAll,
                        isSwapped = isSwapCard,
                        isRandom = isRandomCard,
                    ).collect { resource ->
                        when (resource) {
                            is Resources.Error -> {
                            }

                            is Resources.Loading -> {
                                _uiState.update { it.copy(isLoading = true) }
                            }

                            is Resources.Success -> {
                                if (resource.data.isNullOrEmpty()) {
                                    _uiState.update {
                                        it.copy(
                                            isLoading = false,
                                            isEndOfList = true,
                                            learningTime = 0,
                                        )
                                    }
                                    playCompleteSound()
                                    return@collect
                                }

                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        flashCardList = resource.data
                                    )
                                }
                            }
                        }
                    }
                }

                LearnFrom.FOLDER -> {
                    flashCardRepository.getFlashCardsByFolderId(
                        folderId = folderId,
                        learnMode = LearnMode.FLIP,
                        isGetAll = isGetAll,
                        isSwapped = isSwapCard,
                        isRandom = isRandomCard,
                    ).collect { resource ->
                        when (resource) {
                            is Resources.Error -> {
                            }

                            is Resources.Loading -> {
                                _uiState.update { it.copy(isLoading = true) }
                            }

                            is Resources.Success -> {
                                if (resource.data.isNullOrEmpty()) {
                                    _uiState.update {
                                        it.copy(
                                            isLoading = false,
                                            isEndOfList = true,
                                            learningTime = 0,
                                        )
                                    }
                                    playCompleteSound()
                                    return@collect
                                }

                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        flashCardList = resource.data
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateFlashCardFlipStatus(
        isRight: Boolean,
        flashCardId: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val flipStatus =
                if (isRight) FlipCardStatus.KNOW.name else FlipCardStatus.STILL_LEARNING.name
            flashCardRepository.updateFlipFlashCard(id = flashCardId, flipStatus = flipStatus)
                .collect()
        }
    }

    private fun restartStudySet() {
        val learnFrom = _uiState.value.learnFrom
        when (learnFrom) {
            LearnFrom.STUDY_SET -> {

                viewModelScope.launch {
                    studySetRepository.resetProgress(
                        studySetId = _uiState.value.studySetId,
                        resetType = ResetType.FLIP_STATUS.type
                    ).collect { resource ->
                        when (resource) {
                            is Resources.Error -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        isEndOfList = true,
                                        learningTime = 0,
                                    )
                                }
                            }

                            is Resources.Loading -> {
                                _uiState.update { it.copy(isLoading = true) }
                            }

                            is Resources.Success -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        isEndOfList = false,
                                        countKnown = 0,
                                        countStillLearning = 0,
                                        currentCardIndex = 0,
                                        startTime = System.currentTimeMillis(),
                                        flashCardList = emptyList()
                                    )
                                }
                                getFlashCard()
                            }
                        }
                    }
                }
            }

            LearnFrom.FOLDER -> {

                viewModelScope.launch {
                    folderRepository.resetProgress(
                        folderId = _uiState.value.folderId,
                        resetType = ResetType.FLIP_STATUS.type
                    ).collect { resource ->
                        when (resource) {
                            is Resources.Error -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        isEndOfList = true,
                                        learningTime = 0,
                                    )
                                }
                            }

                            is Resources.Loading -> {
                                _uiState.update { it.copy(isLoading = true) }
                            }

                            is Resources.Success -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        isEndOfList = false,
                                        countKnown = 0,
                                        countStillLearning = 0,
                                        currentCardIndex = 0,
                                        startTime = System.currentTimeMillis(),
                                        flashCardList = emptyList()
                                    )
                                }
                                getFlashCard()
                            }
                        }
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

    private fun sendCompletedStudyTime() {
        val learnFrom = _uiState.value.learnFrom
        when (learnFrom) {
            LearnFrom.STUDY_SET -> {
                viewModelScope.launch {
                    val createStudyTimeModel = CreateStudyTimeModel(
                        learnMode = LearnMode.FLIP.mode,
                        studySetId = _uiState.value.studySetId,
                        timeSpent = _uiState.value.learningTime.toInt(),
                    )
                    studyTimeRepository.createStudyTime(createStudyTimeModel = createStudyTimeModel)
                        .collect()
                }
            }

            LearnFrom.FOLDER -> {
                // TODO(): Implement this
            }
        }
    }
}