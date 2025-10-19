package com.pwhs.quickmem.presentation.app.search

import androidx.annotation.StringRes
import com.pwhs.quickmem.domain.model.search.SearchQueryModel

data class SearchUiState(
    val query: String = "",
    @param:StringRes val error: Int? = null,
    val listResult: List<SearchQueryModel> = emptyList(),
)

