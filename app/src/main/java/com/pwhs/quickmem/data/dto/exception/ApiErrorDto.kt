package com.pwhs.quickmem.data.dto.exception

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorDto(
    @SerialName("statusCode")
    val statusCode: Int,
    @SerialName("message")
    val message: String
)