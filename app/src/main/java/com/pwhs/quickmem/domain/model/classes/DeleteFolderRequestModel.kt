package com.pwhs.quickmem.domain.model.classes

data class DeleteFolderRequestModel(
    val classId: String,
    val folderId: String,
)