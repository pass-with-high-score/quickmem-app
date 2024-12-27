package com.pwhs.quickmem.domain.model.search

data class SearchQueryModel(
    val id: Int,
    val query: String,
    val timestamp: Long,
)