package com.pwhs.quickmem.presentation.app.home.search_by_subject

import androidx.annotation.DrawableRes
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.subject.SubjectModel

data class SearchStudySetBySubjectUiState(
    val id: Int = 0,
    val subject: SubjectModel? = null,
    val studySetCount: Int = 0,
    @DrawableRes val icon: Int = R.drawable.ic_all,
    val isLoading: Boolean = false,
)