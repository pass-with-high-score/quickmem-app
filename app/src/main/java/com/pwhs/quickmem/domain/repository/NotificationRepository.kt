package com.pwhs.quickmem.domain.repository

import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.notification.GetNotificationResponseModel
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun loadNotifications(
        userId: String,

        ): Flow<Resources<List<GetNotificationResponseModel>>>

    suspend fun markNotificationAsRead(
        notificationId: String,

        ): Flow<Resources<Unit>>

    suspend fun deleteNotification(
        notificationId: String,

        ): Flow<Resources<Unit>>

    suspend fun clearAllNotifications(

    ): Flow<Resources<Unit>>
}
