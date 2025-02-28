package com.pwhs.quickmem.data.mapper.flashcard

import com.pwhs.quickmem.data.dto.flashcard.BufferResponseDto
import com.pwhs.quickmem.domain.model.flashcard.BufferResponseModel

fun BufferResponseDto.toModel() = BufferResponseModel(
    type = type,
    data = data
)

fun BufferResponseModel.toDto() = BufferResponseDto(
    type = type,
    data = data
)

fun BufferResponseModel.toByteArray(): ByteArray {
    val byteArray = ByteArray(data.size)
    for (i in data.indices) {
        byteArray[i] = data[i].toByte()
    }
    return byteArray
}