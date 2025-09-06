package com.pwhs.quickmem.data.dto.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarkNotificationReadRequestDto(
    @SerialName("id")
    val id: String,
    @SerialName("isRead")
    val isRead: Boolean
)
