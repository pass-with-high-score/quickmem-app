package com.pwhs.quickmem.data.mapper.flashcard

import com.pwhs.quickmem.data.dto.flashcard.LanguageDto
import com.pwhs.quickmem.domain.model.flashcard.LanguageModel

fun LanguageDto.toModel() = LanguageModel(
    code = code,
    name = name,
    voiceAvailableCount = voiceAvailableCount,
    flag = flag,
    country = country,
)

fun LanguageModel.toDto() = LanguageDto(
    code = code,
    name = name,
    voiceAvailableCount = voiceAvailableCount,
    flag = flag,
    country = country,
)