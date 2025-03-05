package com.pwhs.quickmem.presentation.app.user_detail

import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel

data class UserDetailUiState(
    val isLoading: Boolean = false,
    val isOwner: Boolean = false,
    val userId: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val avatarUrl: String = "",
    val errorMessage: String? = null,
    val studySets: List<GetStudySetResponseModel> = emptyList(),
    val folders: List<GetFolderResponseModel> = emptyList(),
)
