package com.pwhs.quickmem.presentation.app.classes.add_study_set

import kotlinx.serialization.Serializable

@Serializable
data class AddStudySetToClassArgs(
    val classId: String,
)