package com.pwhs.quickmem.data.mapper.auth

import com.pwhs.quickmem.data.dto.auth.UpdateFullNameRequestDto
import com.pwhs.quickmem.domain.model.auth.UpdateFullNameRequestModel

fun UpdateFullNameRequestDto.toModel() = UpdateFullNameRequestModel(
    fullname = fullName
)

fun UpdateFullNameRequestModel.toDto() = UpdateFullNameRequestDto(
    fullName = fullname
)