package com.pwhs.quickmem.data.mapper.user

import com.pwhs.quickmem.data.dto.user.SearchUserResponseDto
import com.pwhs.quickmem.domain.model.users.SearchUserResponseModel

fun SearchUserResponseDto.toModel() = SearchUserResponseModel(
    id = id,
    username = username,
    avatarUrl = avatarUrl,
)

fun SearchUserResponseModel.toDto() = SearchUserResponseDto(
    id = id,
    username = username,
    avatarUrl = avatarUrl,
)