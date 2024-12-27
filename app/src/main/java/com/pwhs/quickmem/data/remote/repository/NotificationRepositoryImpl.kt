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

    override suspend fun loadNotifications(
        userId: String,
        token: String,
    ): Flow<Resources<List<GetNotificationResponseModel>>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val notifications = apiService.getNotificationsByUserId(token, userId)
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
        token: String,
    ): Flow<Resources<Unit>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val requestDto = MarkNotificationReadRequestDto(notificationId, true)
                apiService.markNotificationAsRead(token, notificationId, requestDto)
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
        token: String,
    ): Flow<Resources<Unit>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                apiService.deleteNotification(token, notificationId)
                emit(Resources.Success(Unit))
            } catch (e: HttpException) {
                emit(Resources.Error(e.localizedMessage ?: "Failed to delete notification"))
            } catch (e: IOException) {
                emit(Resources.Error(e.localizedMessage ?: "Network Error"))
            }
        }
    }

}

