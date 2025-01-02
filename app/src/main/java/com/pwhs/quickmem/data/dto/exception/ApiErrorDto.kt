package com.pwhs.quickmem.data.dto.exception

import com.google.gson.annotations.SerializedName

data class ApiErrorDto(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String
)