package com.pwhs.quickmem.presentation.app.folder.edit

import kotlinx.serialization.Serializable

@Serializable
data class EditFolderScreenArgs(
    val folderId: String,
    val folderTitle: String,
    val folderDescription: String,
    val folderIsPublic: Boolean,
)