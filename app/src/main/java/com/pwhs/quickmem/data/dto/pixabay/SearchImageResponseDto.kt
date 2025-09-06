package com.pwhs.quickmem.data.dto.pixabay

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchImageResponseDto(
    @SerialName("total")
    val total: Int,
    @SerialName("totalHits")
    val totalHits: Int,
    @SerialName("images")
    val images: List<PixaBayImageDto>
)
