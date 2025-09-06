package com.pwhs.quickmem.utils

import com.pwhs.quickmem.core.utils.Serde
import com.pwhs.quickmem.data.dto.exception.ApiErrorDto
import retrofit2.HttpException
import timber.log.Timber

fun HttpException.parseApiError(): ApiErrorDto? {
    return try {
        val raw = response()?.errorBody()?.string() ?: return null
        Serde.json.decodeFromString(ApiErrorDto.serializer(), raw)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}