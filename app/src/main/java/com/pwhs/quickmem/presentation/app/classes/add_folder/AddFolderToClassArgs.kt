package com.pwhs.quickmem.presentation.app.classes.add_folder

import kotlinx.serialization.Serializable

@Serializable
data class AddFolderToClassArgs(
    val classId: String,
)