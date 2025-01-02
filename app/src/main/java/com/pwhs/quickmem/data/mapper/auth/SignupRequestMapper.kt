package com.pwhs.quickmem.data.mapper.auth

import com.pwhs.quickmem.data.dto.auth.SignupRequestDto
import com.pwhs.quickmem.domain.model.auth.SignupRequestModel

fun SignupRequestDto.toModel() = SignupRequestModel(
    avatarUrl = avatarUrl?.trim(),
    email = email?.trim()?.lowercase(),
    username = username?.trim(),
    fullName = fullName?.trim(),
    role = role,
    birthday = birthday,
    password = password?.trim(),
    authProvider = provider
)

fun SignupRequestModel.toDto() = SignupRequestDto(
    avatarUrl = avatarUrl?.trim(),
    email = email?.trim()?.lowercase(),
    username = username?.trim(),
    fullName = fullName?.trim(),
    role = role,
    birthday = birthday,
    password = password?.trim(),
    provider = authProvider
)