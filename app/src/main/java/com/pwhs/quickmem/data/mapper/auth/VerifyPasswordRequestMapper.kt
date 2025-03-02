package com.pwhs.quickmem.data.mapper.auth

import com.pwhs.quickmem.data.dto.auth.VerifyPasswordRequestDto
import com.pwhs.quickmem.domain.model.auth.VerifyPasswordRequestModel

fun VerifyPasswordRequestDto.toModel() = VerifyPasswordRequestModel(
    password = password
)

fun VerifyPasswordRequestModel.toDto() = VerifyPasswordRequestDto(
    password = password
)