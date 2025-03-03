package com.pwhs.quickmem.domain.repository

import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.notification.DeviceTokenRequestModel
import kotlinx.coroutines.flow.Flow


interface FirebaseRepository {
    suspend fun sendDeviceToken(
        deviceTokenRequest: DeviceTokenRequestModel,
    ): Flow<Resources<Unit>>
}