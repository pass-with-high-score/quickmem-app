package com.pwhs.quickmem.data.dto.notification

import kotlinx.serialization.SerialName
import com.pwhs.quickmem.core.data.enums.NotificationType
import kotlinx.serialization.Serializable

@Serializable
data class GetNotificationResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("message")
    val message: String,
    @SerialName("userId")
    val userId: String,
    @SerialName("isRead")
    val isRead: Boolean,
    @SerialName("notificationType")
    val notificationType: NotificationType? = NotificationType.NONE,
    @SerialName("data")
    val data: NotificationDataDto? = null,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String
)
