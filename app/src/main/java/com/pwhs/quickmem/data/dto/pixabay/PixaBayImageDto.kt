package com.pwhs.quickmem.data.dto.pixabay

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PixaBayImageDto(
    @SerialName("id")
    val id: Int,
    @SerialName("imageUrl")
    val imageUrl: String,
)
