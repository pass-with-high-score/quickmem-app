package com.pwhs.quickmem.data.mapper.auth

import com.pwhs.quickmem.data.dto.auth.UpdateEmailRequestDto
import com.pwhs.quickmem.domain.model.auth.UpdateEmailRequestModel

fun UpdateEmailRequestDto.toModel() = UpdateEmailRequestModel(
    email = email
)

fun UpdateEmailRequestModel.toDto() = UpdateEmailRequestDto(
    email = email
)

