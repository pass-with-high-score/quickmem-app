package com.pwhs.quickmem.data.mapper.flashcard

import com.pwhs.quickmem.data.dto.flashcard.VoiceDto
import com.pwhs.quickmem.domain.model.flashcard.VoiceModel

fun VoiceDto.toModel() = VoiceModel(
    code = code,
    gender = gender,
    name = name,
)

fun VoiceModel.toDto() = VoiceDto(
    code = code,
    gender = gender,
    name = name,
)