package com.pwhs.quickmem.presentation.app.home.recent.study_set

import androidx.annotation.StringRes
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel

data class AllRecentAccessStudySetsUiState (
    val isLoading: Boolean = false,
    val studySets: List<GetStudySetResponseModel> = emptyList(),
    @StringRes val error: Int? = null
)