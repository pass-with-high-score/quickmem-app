package com.pwhs.quickmem.presentation.app.classes.add_folder

import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel

data class AddFolderToClassUiState(
    val classId: String = "",
    val isImported: Boolean = false,
    val isLoading: Boolean = false,
    val userAvatar: String = "",
    val username: String = "",
    val userId: String = "",
    val folderImportedIds: List<String> = emptyList(),
    val folders: List<GetFolderResponseModel> = emptyList(),
)