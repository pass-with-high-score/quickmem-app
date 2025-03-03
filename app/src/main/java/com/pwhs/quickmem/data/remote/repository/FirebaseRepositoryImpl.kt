package com.pwhs.quickmem.data.remote.repository

import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.data.mapper.notification.toDto
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.model.notification.DeviceTokenRequestModel
import com.pwhs.quickmem.domain.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : FirebaseRepository {
    override suspend fun sendDeviceToken(
        deviceTokenRequest: DeviceTokenRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.sendDeviceToken(
                    tokenRequest = deviceTokenRequest.toDto()
                )
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }
}