package com.pwhs.quickmem.domain.model.folder

data class SaveRecentAccessFolderRequestModel(
    val userId: String,
    val folderId: String,
)