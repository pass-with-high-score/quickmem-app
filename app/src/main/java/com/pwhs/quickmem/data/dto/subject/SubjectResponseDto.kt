package com.pwhs.quickmem.data.dto.subject

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubjectResponseDto(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String? = null,
)