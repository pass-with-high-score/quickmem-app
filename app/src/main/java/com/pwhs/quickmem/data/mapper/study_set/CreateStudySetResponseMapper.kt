package com.pwhs.quickmem.data.mapper.study_set

import com.pwhs.quickmem.data.dto.study_set.CreateStudySetResponseDto
import com.pwhs.quickmem.data.mapper.color.toColorModel
import com.pwhs.quickmem.data.mapper.color.toColorResponseDto
import com.pwhs.quickmem.data.mapper.subject.toSubjectModel
import com.pwhs.quickmem.data.mapper.subject.toSubjectResponseDto
import com.pwhs.quickmem.domain.model.study_set.CreateStudySetResponseModel

fun CreateStudySetResponseModel.toDto() = CreateStudySetResponseDto(
    color = color.toColorResponseDto(),
    description = description.trim(),
    isPublic = isPublic,
    subject = subject.toSubjectResponseDto(),
    title = title.trim(),
    createdAt = createdAt,
    updatedAt = updatedAt,
    isAIGenerated = isAIGenerated,
    id = id
)

fun CreateStudySetResponseDto.toModel() = CreateStudySetResponseModel(
    color = color.toColorModel(),
    description = description.trim(),
    isPublic = isPublic,
    subject = subject.toSubjectModel(),
    title = title.trim(),
    createdAt = createdAt,
    updatedAt = updatedAt,
    isAIGenerated = isAIGenerated,
    id = id
)