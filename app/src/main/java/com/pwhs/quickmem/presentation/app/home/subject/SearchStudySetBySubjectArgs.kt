package com.pwhs.quickmem.presentation.app.home.subject

import androidx.annotation.DrawableRes

data class SearchStudySetBySubjectArgs(
    val id: Int,
    val studySetCount: Int,
    @DrawableRes val icon: Int,
)