package com.pwhs.quickmem.domain.model.folder

data class UpdateFolderRequestModel(
    val title: String,
    val description: String,
    val isPublic: Boolean,
)