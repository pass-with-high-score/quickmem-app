package com.pwhs.quickmem.domain.model.folder

data class AddFolderToClassRequestModel(
    val classId: String,
    val folderIds: List<String>,
)