package com.pwhs.quickmem.presentation.app.home.recent.folder

import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel

data class AllRecentAccessFoldersUiState (
    val isLoading: Boolean = false,
    val folders: List<GetFolderResponseModel> = emptyList(),
)