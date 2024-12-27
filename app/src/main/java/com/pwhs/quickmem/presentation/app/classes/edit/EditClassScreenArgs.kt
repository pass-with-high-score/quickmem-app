package com.pwhs.quickmem.presentation.app.classes.edit

import kotlinx.serialization.Serializable

@Serializable
data class EditClassScreenArgs(
    val classId: String,
    val classTitle: String,
    val classDescription: String,
    val isSetAllowed: Boolean,
    val isMemberAllowed: Boolean,
)