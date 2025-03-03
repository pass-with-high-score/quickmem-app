package com.pwhs.quickmem.presentation.app.ai_generative

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.CoinAction
import com.pwhs.quickmem.core.data.enums.DifficultyLevel
import com.pwhs.quickmem.core.data.enums.QuestionType
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.study_set.CreateStudySetByAIRequestModel
import com.pwhs.quickmem.domain.model.users.UpdateCoinRequestModel
import com.pwhs.quickmem.domain.repository.AuthRepository
import com.pwhs.quickmem.domain.repository.StudySetRepository
import com.pwhs.quickmem.utils.getLanguageCode
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback
import dagger.hilt.android.lifecycle.HiltViewModel
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
class AIGenerativeViewModel @Inject constructor(
    private val appManager: AppManager,
    private val studySetRepository: StudySetRepository,
    private val authRepository: AuthRepository,
    application: Application,
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<AIGenerativeUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val languageCode = getApplication<Application>().getLanguageCode()
        _uiState.update { it.copy(language = languageCode) }
        getUserCoin()
        getCustomerInfo()
    }

    fun onEvent(event: AIGenerativeUiAction) {
        when (event) {
            AIGenerativeUiAction.RefreshTopStreaks -> {
                getUserCoin()
                getCustomerInfo()
            }

            is AIGenerativeUiAction.OnDescriptionChanged -> {
                _uiState.update { it.copy(description = event.description) }
            }

            is AIGenerativeUiAction.OnDifficultyLevelChanged -> {
                _uiState.update { it.copy(difficulty = event.difficultyLevel) }
            }

            is AIGenerativeUiAction.OnLanguageChanged -> {
                _uiState.update { it.copy(language = event.language) }
            }

            is AIGenerativeUiAction.OnNumberOfFlashcardsChange -> {
                _uiState.update {
                    it.copy(
                        numberOfFlashcards = event.numberOfCards
                    )
                }
            }

            is AIGenerativeUiAction.OnQuestionTypeChanged -> {
                _uiState.update { it.copy(questionType = event.questionType) }
            }

            is AIGenerativeUiAction.OnTitleChanged -> {
                _uiState.update { it.copy(title = event.title) }
            }

            is AIGenerativeUiAction.OnCreateStudySet -> {
                if (uiState.value.title.isEmpty()) {
                    _uiState.update {
                        it.copy(errorMessage = R.string.txt_title_is_required)
                    }
                } else {
                    createStudySet()
                }
            }

            is AIGenerativeUiAction.OnEarnCoins -> {
                updateCoins(coinAction = CoinAction.ADD, coin = 1)
            }
        }
    }

    private fun createStudySet() {
        viewModelScope.launch {
            val userId = appManager.userId.firstOrNull() ?: ""
            val createStudySetByAIRequestModel = CreateStudySetByAIRequestModel(
                title = uiState.value.title,
                description = uiState.value.description,
                difficulty = uiState.value.difficulty.level,
                language = uiState.value.language,
                numberOfFlashcards = uiState.value.numberOfFlashcards,
                questionType = uiState.value.questionType.type,
                userId = userId
            )
            studySetRepository.createStudySetByAI(
                createStudySetByAIRequestModel
            ).collect { resource ->
                when (resource) {
                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null,
                                title = "",
                                description = "",
                                numberOfFlashcards = 10,
                                questionType = QuestionType.MULTIPLE_CHOICE,
                                difficulty = DifficultyLevel.EASY,
                                language = getApplication<Application>().getLanguageCode()
                            )
                        }
                        if (_uiState.value.customerInfo?.activeSubscriptions?.isNotEmpty() == false) {
                            updateCoins(
                                coinAction = CoinAction.SUBTRACT,
                                coin = when (uiState.value.numberOfFlashcards) {
                                    in 1..10 -> 1
                                    in 11..20 -> 2
                                    else -> 3
                                }
                            )
                        }
                        _uiEvent.send(
                            AIGenerativeUiEvent.CreatedStudySet(
                                studySetId = resource.data?.id ?: ""
                            )
                        )
                    }

                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = R.string.txt_error_occurred
                            )
                        }
                        _uiEvent.send(AIGenerativeUiEvent.Error(R.string.txt_error_occurred))
                    }
                }
            }
        }
    }

    private fun updateCoins(
        coinAction: CoinAction,
        coin: Int = 1,
    ) {
        viewModelScope.launch {
            authRepository.updateCoin(
                updateCoinRequestModel = UpdateCoinRequestModel(
                    action = coinAction.action,
                    coin = coin
                )
            ).collect { coin ->
                when (coin) {
                    is Resources.Error -> {
                        _uiEvent.send(AIGenerativeUiEvent.Error(R.string.txt_too_many_requests_please_wait_1_minute))
                    }

                    is Resources.Loading -> {
                        // do nothing
                    }

                    is Resources.Success -> {
                        appManager.saveUserCoins(coin.data?.coins ?: 0)
                        _uiState.update {
                            it.copy(coins = coin.data?.coins ?: 0)
                        }
                        _uiEvent.trySend(AIGenerativeUiEvent.EarnedCoins(R.string.txt_you_have_earned_1_coin))
                    }
                }
            }
        }
    }

    private fun getCustomerInfo() {
        Purchases.sharedInstance.getCustomerInfo(object : ReceiveCustomerInfoCallback {
            override fun onReceived(customerInfo: CustomerInfo) {
                _uiState.update {
                    it.copy(
                        customerInfo = customerInfo
                    )
                }
            }

            override fun onError(error: PurchasesError) {
                Timber.e(error.message)
            }
        })
    }

    private fun getUserCoin() {
        viewModelScope.launch {
            val coins = appManager.userCoins.firstOrNull() ?: 0
            val userId = appManager.userId.firstOrNull() ?: ""
            _uiState.update {
                it.copy(
                    coins = coins,
                    ownerId = userId
                )
            }
        }
    }
}