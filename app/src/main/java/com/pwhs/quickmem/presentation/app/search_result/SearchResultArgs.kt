package com.pwhs.quickmem.presentation.app.search_result

import kotlinx.serialization.Serializable

@Serializable
data class SearchResultArgs(
    val query: String,
)