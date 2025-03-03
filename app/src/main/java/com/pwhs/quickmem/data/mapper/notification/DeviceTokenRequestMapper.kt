package com.pwhs.quickmem.data.mapper.notification

import com.pwhs.quickmem.data.dto.notification.DeviceTokenRequestDto
import com.pwhs.quickmem.domain.model.notification.DeviceTokenRequestModel

fun DeviceTokenRequestModel.toDto() = DeviceTokenRequestDto(
    deviceToken = deviceToken
)