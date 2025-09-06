package com.pwhs.quickmem.presentation.app.home.recent.study_set

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.repository.StudySetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentStudySetsViewModel @Inject constructor(
    private val studySetRepository: StudySetRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecentStudySetsUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<RecentStudySetsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getRecentStudySets()
    }

    private fun getRecentStudySets() {
        viewModelScope.launch {
            studySetRepository.getRecentAccessStudySet().collect { resource ->
                when (resource) {
                    is Resources.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is Resources.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            studySets = resource.data ?: emptyList()
                        )
                    }

                    is Resources.Error -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _uiEvent.send(RecentStudySetsUiEvent.Error(resource.message ?: "Unknown error"))
                    }
                }
            }
        }
    }
}