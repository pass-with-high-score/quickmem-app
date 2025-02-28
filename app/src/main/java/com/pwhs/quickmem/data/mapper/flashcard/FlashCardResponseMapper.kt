package com.pwhs.quickmem.data.mapper.flashcard

import com.pwhs.quickmem.data.dto.flashcard.FlashCardResponseDto
import com.pwhs.quickmem.domain.model.flashcard.FlashCardResponseModel

fun FlashCardResponseDto.toModel() = FlashCardResponseModel(
    id = id,
    term = term.trim(),
    termImageURL = termImageURL?.trim(),
    definition = definition.trim(),
    definitionImageURL = definitionImageURL?.trim(),
    hint = hint?.trim(),
    explanation = explanation?.trim(),
    studySetId = studySetId,
    rating = rating,
    isStarred = isStarred,
    flipStatus = flipStatus,
    quizStatus = quizStatus,
    termVoiceCode = termVoiceCode,
    definitionVoiceCode = definitionVoiceCode,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun FlashCardResponseModel.toDto() = FlashCardResponseDto(
    id = id,
    term = term.trim(),
    termImageURL = termImageURL?.trim(),
    definition = definition.trim(),
    definitionImageURL = definitionImageURL?.trim(),
    hint = hint?.trim(),
    explanation = explanation?.trim(),
    studySetId = studySetId,
    rating = rating,
    isStarred = isStarred,
    flipStatus = flipStatus,
    quizStatus = quizStatus,
    termVoiceCode = termVoiceCode,
    definitionVoiceCode = definitionVoiceCode,
    createdAt = createdAt,
    updatedAt = updatedAt,
)