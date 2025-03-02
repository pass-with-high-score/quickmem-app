package com.pwhs.quickmem.data.mapper.classes

import com.pwhs.quickmem.data.dto.classes.RemoveMembersRequestDto
import com.pwhs.quickmem.domain.model.classes.RemoveMembersRequestModel

fun RemoveMembersRequestDto.toModel() = RemoveMembersRequestModel(
    classId = classId,
    memberIds = memberIds
)

fun RemoveMembersRequestModel.toDto() = RemoveMembersRequestDto(
    classId = classId,
    memberIds = memberIds
)