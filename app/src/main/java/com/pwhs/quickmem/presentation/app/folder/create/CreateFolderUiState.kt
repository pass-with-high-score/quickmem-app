package com.pwhs.quickmem.presentation.app.folder.create

import androidx.annotation.StringRes

data class CreateFolderUiState(
    val isLoading: Boolean = false,
    val title: String = "",
    @param:StringRes val titleError: Int? = null,
    val description: String = "",
    val isPublic: Boolean = false,
)