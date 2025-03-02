package com.pwhs.quickmem.data.mapper.auth

import com.pwhs.quickmem.data.dto.auth.ChangeRoleRequestDto
import com.pwhs.quickmem.domain.model.auth.ChangeRoleRequestModel

fun ChangeRoleRequestDto.toModel() = ChangeRoleRequestModel(
    role = role,
)

fun ChangeRoleRequestModel.toDto() = ChangeRoleRequestDto(
    role = role,
)