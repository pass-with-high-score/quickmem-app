package com.pwhs.quickmem.data.mapper.study_set

import com.pwhs.quickmem.data.dto.study_set.CreateStudySetByAIRequestDto
import com.pwhs.quickmem.domain.model.study_set.CreateStudySetByAIRequestModel

fun CreateStudySetByAIRequestModel.toDto() = CreateStudySetByAIRequestDto(
    description = description.trim(),
    difficulty = difficulty,
    language = language,
    numberOfFlashcards = numberOfFlashcards,
    questionType = questionType,
    title = title.trim(),
)

fun CreateStudySetByAIRequestDto.toModel() = CreateStudySetByAIRequestModel(
    description = description.trim(),
    difficulty = difficulty,
    language = language,
    numberOfFlashcards = numberOfFlashcards,
    questionType = questionType,
    title = title.trim(),
)