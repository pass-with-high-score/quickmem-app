package com.pwhs.quickmem.data.mapper.study_time

import com.pwhs.quickmem.data.dto.study_time.CreateStudyTimeDto
import com.pwhs.quickmem.domain.model.study_time.CreateStudyTimeModel

fun CreateStudyTimeModel.toDto(): CreateStudyTimeDto {
    return CreateStudyTimeDto(
        learnMode = learnMode,
        studySetId = studySetId,
        timeSpent = timeSpent,
    )
}

fun CreateStudyTimeDto.toModel(): CreateStudyTimeModel {
    return CreateStudyTimeModel(
        learnMode = learnMode,
        studySetId = studySetId,
        timeSpent = timeSpent,
    )
}