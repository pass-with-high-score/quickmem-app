package com.pwhs.quickmem.data.mapper.classes

import com.pwhs.quickmem.data.dto.classes.JoinClassRequestDto
import com.pwhs.quickmem.domain.model.classes.JoinClassRequestModel

fun JoinClassRequestDto.toModel() = JoinClassRequestModel(
    joinToken = joinToken,
    classId = classId
)

fun JoinClassRequestModel.toDto() = JoinClassRequestDto(
    joinToken = joinToken,
    classId = classId
)