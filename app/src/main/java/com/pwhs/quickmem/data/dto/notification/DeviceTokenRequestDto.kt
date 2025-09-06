package com.pwhs.quickmem.data.dto.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceTokenRequestDto(
    @SerialName("deviceToken")
    val deviceToken: String
)
