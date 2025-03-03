package com.pwhs.quickmem.presentation.app.study_set.add_to_folder

import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel

data class AddStudySetToFoldersUiState (
    val studySetId : String = "",
    val isImported: Boolean = false,
    val isLoading : Boolean = false,
    val userAvatar: String = "",
    val username: String = "",
    val folderImportedIds: List<String> = emptyList(),
    val folders: List<GetFolderResponseModel> = emptyList(),
)