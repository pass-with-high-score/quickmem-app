package com.pwhs.quickmem.data.mapper.classes

import com.pwhs.quickmem.data.dto.classes.DeleteStudySetsRequestDto
import com.pwhs.quickmem.domain.model.classes.DeleteStudySetsRequestModel

fun DeleteStudySetsRequestDto.toModel() = DeleteStudySetsRequestModel(
    classId = classId,
    studySetId = studySetId
)

fun DeleteStudySetsRequestModel.toDto() = DeleteStudySetsRequestDto(
    classId = classId,
    studySetId = studySetId
)