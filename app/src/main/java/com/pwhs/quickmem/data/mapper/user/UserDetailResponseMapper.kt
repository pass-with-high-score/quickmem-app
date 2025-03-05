package com.pwhs.quickmem.data.mapper.user

import com.pwhs.quickmem.data.dto.user.UserDetailResponseDto
import com.pwhs.quickmem.data.mapper.folder.toDto
import com.pwhs.quickmem.data.mapper.folder.toModel
import com.pwhs.quickmem.data.mapper.study_set.toDto
import com.pwhs.quickmem.data.mapper.study_set.toModel
import com.pwhs.quickmem.domain.model.users.UserDetailResponseModel

fun UserDetailResponseDto.toModel(): UserDetailResponseModel {
    return UserDetailResponseModel(
        id = id,
        avatarUrl = avatarUrl,
        folders = folders.map { it.toModel() },
        fullName = fullName,
        role = role,
        studySets = studySets.map { it.toModel() },
        username = username,
    )
}

fun UserDetailResponseModel.toDto(): UserDetailResponseDto {
    return UserDetailResponseDto(
        id = id,
        avatarUrl = avatarUrl,
        folders = folders.map { it.toDto() },
        fullName = fullName,
        role = role,
        studySets = studySets.map { it.toDto() },
        username = username,
    )
}