package com.pwhs.quickmem.data.mapper.flashcard

import com.pwhs.quickmem.data.dto.flashcard.CreateFlashCardDto
import com.pwhs.quickmem.domain.model.flashcard.CreateFlashCardModel

fun CreateFlashCardDto.toModel() = CreateFlashCardModel(
    term = term.trim(),
    termImageURL = termImageURL,
    termVoiceCode = termVoiceCode,
    definition = definition.trim(),
    definitionImageURL = definitionImageURL,
    definitionVoiceCode = definitionVoiceCode,
    hint = hint?.trim(),
    explanation = explanation?.trim(),
    studySetId = studySetId,
)

fun CreateFlashCardModel.toDto() = CreateFlashCardDto(
    term = term.trim(),
    termImageURL = termImageURL,
    termVoiceCode = termVoiceCode,
    definition = definition.trim(),
    definitionImageURL = definitionImageURL,
    definitionVoiceCode = definitionVoiceCode,
    hint = hint?.trim(),
    explanation = explanation?.trim(),
    studySetId = studySetId,
)