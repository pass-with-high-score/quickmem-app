package com.pwhs.quickmem.domain.model.classes

data class AddFoldersToClassRequestModel(
    val userId: String,
    val classId: String,
    val folderIds: List<String>,
)