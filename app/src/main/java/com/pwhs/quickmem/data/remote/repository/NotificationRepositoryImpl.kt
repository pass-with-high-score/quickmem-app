package com.pwhs.quickmem.data.remote.repository

import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.data.dto.notification.MarkNotificationReadRequestDto
import com.pwhs.quickmem.data.mapper.notification.toModel
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.model.notification.GetNotificationResponseModel
import com.pwhs.quickmem.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : NotificationRepository {

    override suspend fun loadNotifications(): Flow<Resources<List<GetNotificationResponseModel>>> {
        return flow {
            emit(Resources.Loading())
            try {
                val notifications = apiService.getNotificationsByUserId()
                    .map { it.toModel() }
                emit(Resources.Success(notifications))
            } catch (e: HttpException) {
                emit(Resources.Error(e.localizedMessage ?: "Failed to load notifications"))
            } catch (e: IOException) {
                emit(Resources.Error(e.localizedMessage ?: "Network Error"))
            }
        }
    }


    override suspend fun markNotificationAsRead(
        notificationId: String,
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                val requestDto = MarkNotificationReadRequestDto(notificationId, true)
                apiService.markNotificationAsRead(notificationId, requestDto)
                emit(Resources.Success(Unit))
            } catch (e: HttpException) {
                emit(Resources.Error(e.localizedMessage ?: "Failed to mark notification as read"))
            } catch (e: IOException) {
                emit(Resources.Error(e.localizedMessage ?: "Network Error"))
            }
        }
    }

    override suspend fun deleteNotification(
        notificationId: String,
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.deleteNotification(notificationId)
                emit(Resources.Success(Unit))
            } catch (e: HttpException) {
                emit(Resources.Error(e.localizedMessage ?: "Failed to delete notification"))
            } catch (e: IOException) {
                emit(Resources.Error(e.localizedMessage ?: "Network Error"))
            }
        }
    }

    override suspend fun clearAllNotifications(): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.clearAllNotifications()
                emit(Resources.Success(Unit))
            } catch (e: HttpException) {
                emit(Resources.Error(e.localizedMessage ?: "Failed to clear all notifications"))
            } catch (e: IOException) {
                emit(Resources.Error(e.localizedMessage ?: "Network Error"))
            }
        }
    }

}

