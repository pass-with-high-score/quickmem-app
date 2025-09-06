package com.pwhs.quickmem.data.dto.color

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ColorResponseDto(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("hexValue")
    val hexValue: String
)