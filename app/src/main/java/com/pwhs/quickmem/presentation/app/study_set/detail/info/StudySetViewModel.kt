package com.pwhs.quickmem.presentation.app.study_set.detail.info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class StudySetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(StudySetInfoUiState())
    val uiState = _uiState

    init {
        val title = savedStateHandle.get<String>("title") ?: ""
        val description = savedStateHandle.get<String>("description") ?: ""
        val isPublic = savedStateHandle.get<Boolean>("isPublic") == true
        val authorUsername = savedStateHandle.get<String>("authorUsername") ?: ""
        val authorAvatarUrl = savedStateHandle.get<String>("authorAvatarUrl") ?: ""
        val creationDate = savedStateHandle.get<String>("creationDate") ?: ""
        val isAIGenerated = savedStateHandle.get<Boolean>("isAIGenerated") == true

        _uiState.value = StudySetInfoUiState(
            title = title,
            description = description,
            isPublic = isPublic,
            authorUsername = authorUsername,
            authorAvatarUrl = authorAvatarUrl,
            creationDate = creationDate,
            isAIGenerated = isAIGenerated
        )
    }
}