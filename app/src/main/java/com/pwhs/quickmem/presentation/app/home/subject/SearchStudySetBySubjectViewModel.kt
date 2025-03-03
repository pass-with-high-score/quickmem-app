package com.pwhs.quickmem.presentation.app.home.subject

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.domain.model.subject.SubjectModel
import com.pwhs.quickmem.domain.repository.StudySetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchStudySetBySubjectViewModel @Inject constructor(
    private val studySetRepository: StudySetRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchStudySetBySubjectUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<SearchStudySetBySubjectUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var job: Job? = null

    private val _studySetState: MutableStateFlow<PagingData<GetStudySetResponseModel>> =
        MutableStateFlow(PagingData.empty())
    val studySetState: MutableStateFlow<PagingData<GetStudySetResponseModel>> = _studySetState

    init {
        val id = savedStateHandle.get<Int>("id") ?: 0
        val studySetCount = savedStateHandle.get<Int>("studySetCount") ?: 0
        val icon = savedStateHandle.get<Int>("icon") ?: 0
        _uiState.update {
            it.copy(
                id = id,
                studySetCount = studySetCount,
                icon = icon
            )
        }
        val subject = SubjectModel.defaultSubjects.find { it.id == id }
        _uiState.update { it.copy(subject = subject) }

        searchStudySetBySubject()
    }

    fun onEvent(event: SearchStudySetBySubjectUiAction) {
        when (event) {
            SearchStudySetBySubjectUiAction.RefreshStudySets -> {
                job?.cancel()
                job = viewModelScope.launch {
                    delay(500)
                    searchStudySetBySubject()
                }
            }
        }
    }

    private fun searchStudySetBySubject() {
        viewModelScope.launch {
            try {
                studySetRepository.getStudySetBySubjectId(
                    subjectId = _uiState.value.id,
                    page = 1
                ).distinctUntilChanged()
                    .onStart {
                        _uiState.update { it.copy(isLoading = true) }
                        _studySetState.value = PagingData.empty()
                    }
                    .cachedIn(viewModelScope)
                    .onCompletion {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                    .collect { pagingData ->
                        _studySetState.value = pagingData
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _uiEvent.send(
                    SearchStudySetBySubjectUiEvent.Error(
                        e.message ?: "An error occurred"
                    )
                )
            }
        }
    }
}