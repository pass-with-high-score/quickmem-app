package com.pwhs.quickmem.data.mapper.auth

import com.pwhs.quickmem.data.dto.auth.LoginRequestDto
import com.pwhs.quickmem.domain.model.auth.LoginRequestModel

fun LoginRequestDto.toModel() = LoginRequestModel(
    email = email?.trim()?.lowercase(),
    password = password?.trim(),
    authProvider = provider,
    idToken = idToken
)

fun LoginRequestModel.toDto() = LoginRequestDto(
    email = email?.trim()?.lowercase(),
    password = password?.trim(),
    provider = authProvider,
    idToken = idToken
)
