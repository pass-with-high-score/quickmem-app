package com.pwhs.quickmem.data.mapper.study_set

import com.pwhs.quickmem.data.dto.study_set.GetStudySetResponseDto
import com.pwhs.quickmem.data.mapper.color.toColorModel
import com.pwhs.quickmem.data.mapper.color.toColorResponseDto
import com.pwhs.quickmem.data.mapper.flashcard.toDto
import com.pwhs.quickmem.data.mapper.flashcard.toModel
import com.pwhs.quickmem.data.mapper.subject.toSubjectModel
import com.pwhs.quickmem.data.mapper.subject.toSubjectResponseDto
import com.pwhs.quickmem.data.mapper.user.toDto
import com.pwhs.quickmem.data.mapper.user.toModel
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel

fun GetStudySetResponseDto.toModel() = GetStudySetResponseModel(
    color = color?.toColorModel(),
    description = description?.trim(),
    id = id,
    isPublic = isPublic,
    subject = subject?.toSubjectModel(),
    title = title.trim(),
    createdAt = createdAt,
    updatedAt = updatedAt,
    flashcardCount = flashcardCount,
    flashcards = flashcards?.map { it.toModel() }?.sortedByDescending { it.createdAt } ?: emptyList(),
    owner = owner.toModel(),
    linkShareCode = linkShareCode,
    isImported = isImported,
    isAIGenerated = isAIGenerated,
    previousDefinitionVoiceCode = previousDefinitionVoiceCode,
    previousTermVoiceCode = previousTermVoiceCode,
)

fun GetStudySetResponseModel.toDto() = GetStudySetResponseDto(
    color = color?.toColorResponseDto(),
    description = description?.trim(),
    id = id,
    isPublic = isPublic,
    subject = subject?.toSubjectResponseDto(),
    title = title.trim(),
    createdAt = createdAt,
    updatedAt = updatedAt,
    flashcardCount = flashcardCount,
    flashcards = flashcards.map { it.toDto() },
    owner = owner.toDto(),
    linkShareCode = linkShareCode,
    isImported = isImported,
    isAIGenerated = isAIGenerated,
    previousDefinitionVoiceCode = previousDefinitionVoiceCode,
    previousTermVoiceCode = previousTermVoiceCode,
)