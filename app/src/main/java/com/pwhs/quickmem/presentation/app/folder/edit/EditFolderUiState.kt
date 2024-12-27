package com.pwhs.quickmem.presentation.app.folder.edit

import androidx.annotation.StringRes

data class EditFolderUiState(
    val id: String = "",
    val title: String = "",
    @StringRes val titleError: Int? = null,
    val description: String = "",
    val isPublic: Boolean = false,
    val isLoading: Boolean = false,
)