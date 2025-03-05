package com.pwhs.quickmem.data.dto.auth

import com.google.gson.annotations.SerializedName

data class GetUserProfileResponseDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("avatarUrl")
    val avatarUrl: String,
    @SerializedName("coin")
    val coin: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("bannedAt")
    val bannedAt: String? = null,
    @SerializedName("userStatus")
    val userStatus: String? = null,
    @SerializedName("bannedReason")
    val bannedReason: String? = null,
    @SerializedName("studySetCount")
    val studySetCount: Int,
    @SerializedName("folderCount")
    val folderCount: Int,
)
