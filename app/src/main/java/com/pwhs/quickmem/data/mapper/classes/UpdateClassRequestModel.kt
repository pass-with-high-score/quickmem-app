package com.pwhs.quickmem.data.mapper.classes

import com.pwhs.quickmem.data.dto.classes.UpdateClassRequestDto
import com.pwhs.quickmem.domain.model.classes.UpdateClassRequestModel

fun UpdateClassRequestDto.toModel() = UpdateClassRequestModel(
    title = title,
    description = description,
    allowSetManagement = allowSetManagement,
    allowMemberManagement = allowMemberManagement,
)

fun UpdateClassRequestModel.toDto() = UpdateClassRequestDto(
    title = title,
    description = description,
    allowSetManagement = allowSetManagement,
    allowMemberManagement = allowMemberManagement,
)