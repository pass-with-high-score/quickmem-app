package com.pwhs.quickmem.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.streak.StreakModel
import com.pwhs.quickmem.domain.repository.StreakRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class StandardViewModel @Inject constructor(
    private val streakRepository: StreakRepository,
    private val tokenManager: TokenManager,
    private val appManager: AppManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StandardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            tokenManager.accessToken.collectLatest { token ->
                if (!token.isNullOrEmpty()) {
                    appManager.userId.collectLatest { userId ->
                        if (userId.isNotEmpty()) {
                            getStreaksByUserId(token, userId)
                            updateStreak(token, userId)
                        }
                    }
                }
            }
        }
    }

    private fun getStreaksByUserId(token: String, userId: String) {
        viewModelScope.launch {
            streakRepository.getStreaksByUserId(token, userId).collect { resource ->
                when (resource) {
                    is Resources.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is Resources.Success -> {
                        val streaks = resource.data?.streaks ?: emptyList()
                        val streakDates = calculateStreakDates(streaks)

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            streaks = streaks,
                            streakDates = streakDates
                        )
                        Timber.d("Dates: $streakDates")
                    }

                    is Resources.Error -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                }
            }
        }
    }

    private fun updateStreak(token: String, userId: String) {
        viewModelScope.launch {
            streakRepository.updateStreak(token, userId).collect { resource ->
                when (resource) {
                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                streakCount = resource.data?.streakCount ?: 0
                            )
                        }
                    }

                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }
                }
            }
        }
    }

    private fun calculateStreakDates(streaks: List<StreakModel>): List<LocalDate> {
        return streaks.flatMap { streak ->
            val firstStreakDate = OffsetDateTime.parse(streak.date).toLocalDate()
            (0 until streak.streakCount).map {
                firstStreakDate.minusDays(it.toLong())
            }
        }.distinct()
    }
}