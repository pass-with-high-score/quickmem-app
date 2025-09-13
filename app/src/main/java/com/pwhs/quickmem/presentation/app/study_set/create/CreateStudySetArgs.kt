package com.pwhs.quickmem.presentation.app.study_set.create

import kotlinx.serialization.Serializable

@Serializable
data class CreateStudySetArgs(
    val subjectId: Int? = null,
)