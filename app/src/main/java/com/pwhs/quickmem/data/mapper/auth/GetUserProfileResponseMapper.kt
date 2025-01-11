package com.pwhs.quickmem.data.mapper.auth

import com.pwhs.quickmem.data.dto.auth.GetUserProfileResponseDto
import com.pwhs.quickmem.domain.model.auth.GetUserProfileResponseModel

fun GetUserProfileResponseDto.toModel() = GetUserProfileResponseModel(
    id = id,
    role = role,
    email = email,
    fullname = fullname,
    username = username,
    avatarUrl = avatarUrl,
    createdAt = createdAt,
    updatedAt = updatedAt,
    coin = coin,
    bannedReason = bannedReason,
    bannedAt = bannedAt,
    userStatus = userStatus,
    studySetCount = studySetCount,
    folderCount = folderCount
)

fun GetUserProfileResponseModel.toDto() = GetUserProfileResponseDto(
    id = id,
    role = role,
    email = email,
    fullname = fullname,
    username = username,
    avatarUrl = avatarUrl,
    createdAt = createdAt,
    updatedAt = updatedAt,
    coin = coin,
    bannedReason = bannedReason,
    bannedAt = bannedAt,
    userStatus = userStatus,
    studySetCount = studySetCount,
    folderCount = folderCount
)