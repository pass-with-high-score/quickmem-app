package com.pwhs.quickmem.data.mapper.study_set

import com.pwhs.quickmem.data.dto.study_set.AddStudySetToClassRequestDto
import com.pwhs.quickmem.domain.model.study_set.AddStudySetToClassRequestModel

fun AddStudySetToClassRequestModel.toDto() = AddStudySetToClassRequestDto(
    classId = classId,
    studySetIds = studySetIds
)

fun AddStudySetToClassRequestDto.toModel() = AddStudySetToClassRequestModel(
    classId = classId,
    studySetIds = studySetIds
)