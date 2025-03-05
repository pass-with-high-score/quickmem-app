package com.pwhs.quickmem.data.mapper.auth

import com.pwhs.quickmem.data.dto.auth.AuthResponseDto
import com.pwhs.quickmem.domain.model.auth.AuthResponseModel

fun AuthResponseDto.toModel() = AuthResponseModel(
    id = id,
    fullName = fullName?.trim(),
    email = email?.trim(),
    username = username?.trim(),
    avatarUrl = avatarUrl?.trim(),
    birthday = birthday,
    accessToken = accessToken,
    refreshToken = refreshToken,
    provider = provider,
    isVerified = isVerified,
    coin = coin,
    bannedAt = bannedAt,
    userStatus = userStatus,
    bannedReason = bannedReason?.trim()
)