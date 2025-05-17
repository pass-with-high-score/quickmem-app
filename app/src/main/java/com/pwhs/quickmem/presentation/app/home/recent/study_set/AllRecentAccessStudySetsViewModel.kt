package com.pwhs.quickmem.presentation.app.home.recent.study_set

import androidx.lifecycle.SavedStateHandle
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
class AllRecentAccessStudySetsViewModel @Inject constructor(
    private val studySetRepository: StudySetRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AllRecentAccessStudySetsUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<AllRecentAccessStudySetsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getAllRecentAccessStudySets()
    }

    private fun getAllRecentAccessStudySets() {
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
                    }
                }
            }
        }
    }
}