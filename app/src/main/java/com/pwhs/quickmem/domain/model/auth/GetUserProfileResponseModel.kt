package com.pwhs.quickmem.domain.model.auth

data class GetUserProfileResponseModel(
    val id: String,
    val username: String,
    val fullname: String,
    val email: String,
    val avatarUrl: String,
    val coin: Int,
    val createdAt: String,
    val updatedAt: String,
    val bannedAt: String? = null,
    val userStatus: String? = null,
    val bannedReason: String? = null,
    val studySetCount: Int,
    val folderCount: Int,
)