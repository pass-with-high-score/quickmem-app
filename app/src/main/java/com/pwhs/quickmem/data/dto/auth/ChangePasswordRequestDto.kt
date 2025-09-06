package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ChangePasswordRequestDto(
    @SerialName("oldPassword")
    val oldPassword: String,
    @SerialName("newPassword")
    val newPassword: String,
)