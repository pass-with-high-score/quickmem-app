package com.pwhs.quickmem.data.mapper.classes

import com.pwhs.quickmem.data.dto.classes.ExitClassRequestDto
import com.pwhs.quickmem.domain.model.classes.ExitClassRequestModel

fun ExitClassRequestModel.toDto() = ExitClassRequestDto(
    classId = classId
)

fun ExitClassRequestDto.toModel() = ExitClassRequestModel(
    classId = classId
)