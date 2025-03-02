package com.pwhs.quickmem.data.mapper.auth

import com.pwhs.quickmem.data.dto.auth.ChangePasswordRequestDto
import com.pwhs.quickmem.domain.model.auth.ChangePasswordRequestModel

fun ChangePasswordRequestDto.toModel() = ChangePasswordRequestModel(
    oldPassword = oldPassword,
    newPassword = newPassword
)

fun ChangePasswordRequestModel.toDto() = ChangePasswordRequestDto(
    oldPassword = oldPassword,
    newPassword = newPassword
)