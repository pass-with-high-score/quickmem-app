package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateFullNameRequestDto(
    @SerialName("fullname")
    val fullName: String,
)