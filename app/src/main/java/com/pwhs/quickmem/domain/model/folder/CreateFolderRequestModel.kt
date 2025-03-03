package com.pwhs.quickmem.domain.model.folder

data class CreateFolderRequestModel(
    val description: String,
    val isPublic: Boolean,
    val title: String,
)
