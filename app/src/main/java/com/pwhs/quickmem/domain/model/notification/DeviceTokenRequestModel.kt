package com.pwhs.quickmem.domain.model.notification

data class DeviceTokenRequestModel(
    val userId: String,
    val deviceToken: String,
)
