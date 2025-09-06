package com.pwhs.quickmem.data.dto.upload

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteImageDto(
    @SerialName("imageURL")
    val imageURL: String? = null
)