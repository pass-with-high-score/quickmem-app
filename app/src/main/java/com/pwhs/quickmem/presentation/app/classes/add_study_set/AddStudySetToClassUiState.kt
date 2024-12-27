package com.pwhs.quickmem.presentation.app.classes.add_study_set

import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel

data class AddStudySetToClassUiState(
    val classId: String = "",
    val isImported: Boolean = false,
    val isLoading: Boolean = false,
    val userAvatar: String = "",
    val username: String = "",
    val token: String = "",
    val userId: String = "",
    val studySetImportedIds: List<String> = emptyList(),
    val studySets: List<GetStudySetResponseModel> = emptyList(),
)