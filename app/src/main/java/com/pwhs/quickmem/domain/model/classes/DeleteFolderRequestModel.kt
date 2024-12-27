package com.pwhs.quickmem.domain.model.classes

data class DeleteFolderRequestModel(
    val userId: String,
    val classId: String,
    val folderId: String,
)