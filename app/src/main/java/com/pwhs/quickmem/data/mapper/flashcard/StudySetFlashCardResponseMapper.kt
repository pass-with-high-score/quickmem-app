package com.pwhs.quickmem.data.mapper.flashcard

import com.pwhs.quickmem.data.dto.flashcard.StudySetFlashCardResponseDto
import com.pwhs.quickmem.domain.model.flashcard.StudySetFlashCardResponseModel

fun StudySetFlashCardResponseDto.toModel() = StudySetFlashCardResponseModel(
    id = id,
    term = term.trim(),
    termImageURL = termImageURL,
    definition = definition.trim(),
    definitionImageURL = definitionImageURL,
    hint = hint?.trim(),
    explanation = explanation?.trim(),
    rating = rating,
    flipStatus = flipStatus,
    quizStatus = quizStatus,
    trueFalseStatus = trueFalseStatus,
    writeStatus = writeStatus,
    isStarred = isStarred,
    isAIGenerated = isAIGenerated,
    termVoiceCode = termVoiceCode,
    definitionVoiceCode = definitionVoiceCode,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun StudySetFlashCardResponseModel.toDto() = StudySetFlashCardResponseDto(
    id = id,
    term = term.trim(),
    termImageURL = termImageURL,
    definition = definition.trim(),
    definitionImageURL = definitionImageURL,
    hint = hint?.trim(),
    explanation = explanation?.trim(),
    rating = rating,
    flipStatus = flipStatus,
    quizStatus = quizStatus,
    trueFalseStatus = trueFalseStatus,
    writeStatus = writeStatus,
    isStarred = isStarred,
    isAIGenerated = isAIGenerated,
    termVoiceCode = termVoiceCode,
    definitionVoiceCode = definitionVoiceCode,
    createdAt = createdAt,
    updatedAt = updatedAt,
)