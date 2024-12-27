package com.pwhs.quickmem.domain.model.notification

import com.pwhs.quickmem.core.data.enums.NotificationType

data class GetNotificationResponseModel(
    val id: String,
    val title: String,
    val message: String,
    val userId: String,
    val notificationType: NotificationType? = NotificationType.NONE,
    val data: NotificationDataModel? = null,
    val isRead: Boolean,
    val createdAt: String,
    val updatedAt: String,
)