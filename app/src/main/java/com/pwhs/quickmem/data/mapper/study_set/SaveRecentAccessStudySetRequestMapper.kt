package com.pwhs.quickmem.data.mapper.study_set

import com.pwhs.quickmem.data.dto.study_set.SaveRecentAccessStudySetRequestDto
import com.pwhs.quickmem.domain.model.study_set.SaveRecentAccessStudySetRequestModel

fun SaveRecentAccessStudySetRequestModel.toDto() = SaveRecentAccessStudySetRequestDto(
    studySetId = studySetId
)

fun SaveRecentAccessStudySetRequestDto.toModel() = SaveRecentAccessStudySetRequestModel(
    studySetId = studySetId
)