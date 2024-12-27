package com.pwhs.quickmem.presentation.app.folder.detail

import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.domain.model.users.UserResponseModel

data class FolderDetailUiState(
    val id: String = "",
    val title: String = "",
    val linkShareCode: String = "",
    val description: String = "",
    val isPublic: Boolean = false,
    val studySetCount: Int = 0,
    val ownerId: String = "",
    val user: UserResponseModel = UserResponseModel(),
    val studySets: List<GetStudySetResponseModel> = emptyList(),
    val createdAt: String = "",
    val updatedAt: String = "",
    val isLoading: Boolean = false,
    val isOwner: Boolean = false,
    val totalFlashCards: Int = 0,
)