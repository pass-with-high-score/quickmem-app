package com.pwhs.quickmem.presentation.app.library

import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel

data class LibraryUiState(
    val title: String = "",
    val isLoading: Boolean = false,
    val userAvatarUrl: String = "",
    val username: String = "",
    val studySets: List<GetStudySetResponseModel> = emptyList(),
    val folders: List<GetFolderResponseModel> = emptyList(),
)
