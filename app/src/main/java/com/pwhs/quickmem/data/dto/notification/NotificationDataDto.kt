package com.pwhs.quickmem.data.dto.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDataDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("code")
    val code: String? = null,
)
