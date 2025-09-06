package com.pwhs.quickmem.presentation.app.home.recent.folder

import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel

data class RecentFoldersUiState (
    val isLoading: Boolean = false,
    val folders: List<GetFolderResponseModel> = emptyList(),
)