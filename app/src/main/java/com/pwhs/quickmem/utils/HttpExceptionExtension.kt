package com.pwhs.quickmem.utils

import com.google.gson.Gson
import com.pwhs.quickmem.data.dto.exception.ApiErrorDto
import retrofit2.HttpException

fun HttpException.parseApiError(): ApiErrorDto? {
    return try {
        val errorBody = this.response()?.errorBody()?.string()
        Gson().fromJson(errorBody, ApiErrorDto::class.java)
    } catch (ex: Exception) {
        null
    }
}