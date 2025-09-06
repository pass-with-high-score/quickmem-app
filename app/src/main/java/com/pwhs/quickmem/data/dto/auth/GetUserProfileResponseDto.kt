package com.pwhs.quickmem.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserProfileResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("username")
    val username: String,
    @SerialName("fullname")
    val fullname: String,
    @SerialName("email")
    val email: String,
    @SerialName("avatarUrl")
    val avatarUrl: String,
    @SerialName("coin")
    val coin: Int,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("bannedAt")
    val bannedAt: String? = null,
    @SerialName("userStatus")
    val userStatus: String? = null,
    @SerialName("bannedReason")
    val bannedReason: String? = null,
    @SerialName("studySetCount")
    val studySetCount: Int,
    @SerialName("folderCount")
    val folderCount: Int,
)
