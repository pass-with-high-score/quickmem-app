package com.pwhs.quickmem.data.mapper.auth

import com.pwhs.quickmem.data.dto.auth.UpdateUsernameRequestDto
import com.pwhs.quickmem.domain.model.auth.UpdateUsernameRequestModel

fun UpdateUsernameRequestDto.toModel() = UpdateUsernameRequestModel(
    newUsername = newUsername
)

fun UpdateUsernameRequestModel.toDto() = UpdateUsernameRequestDto(
    newUsername = newUsername
)