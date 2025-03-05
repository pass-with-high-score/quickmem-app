package com.pwhs.quickmem.data.mapper.study_set

import com.pwhs.quickmem.data.dto.study_set.CreateStudySetRequestDto
import com.pwhs.quickmem.domain.model.study_set.CreateStudySetRequestModel

fun CreateStudySetRequestModel.toDto() = CreateStudySetRequestDto(
    colorId = colorId,
    description = description.trim(),
    isPublic = isPublic,
    subjectId = subjectId,
    title = title.trim()
)

fun CreateStudySetRequestDto.toModel() = CreateStudySetRequestModel(
    colorId = colorId,
    description = description.trim(),
    isPublic = isPublic,
    subjectId = subjectId,
    title = title.trim()
)