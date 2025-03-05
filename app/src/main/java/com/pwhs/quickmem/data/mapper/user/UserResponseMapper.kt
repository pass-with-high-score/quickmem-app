package com.pwhs.quickmem.data.mapper.user

import com.pwhs.quickmem.data.dto.user.UserResponseDto
import com.pwhs.quickmem.domain.model.users.UserResponseModel

fun UserResponseDto.toModel() = UserResponseModel(
    id = id,
    avatarUrl = avatarUrl,
    username = username
)

fun UserResponseModel.toDto() = UserResponseDto(
    id = id,
    avatarUrl = avatarUrl,
    username = username
)