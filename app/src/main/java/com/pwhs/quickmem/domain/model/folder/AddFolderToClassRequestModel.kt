package com.pwhs.quickmem.domain.model.folder

data class AddFolderToClassRequestModel(
    val userId: String,
    val classId: String,
    val folderIds: List<String>,
)