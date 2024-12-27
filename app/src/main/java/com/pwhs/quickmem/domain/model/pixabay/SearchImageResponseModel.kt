package com.pwhs.quickmem.domain.model.pixabay

data class SearchImageResponseModel(
    val total: Int,
    val totalHits: Int,
    val images: List<PixaBayImageModel>,
)
