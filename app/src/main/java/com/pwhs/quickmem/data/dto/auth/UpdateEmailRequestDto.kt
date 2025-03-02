package com.pwhs.quickmem.data.dto.auth

import com.google.gson.annotations.SerializedName

data class UpdateEmailRequestDto(
    @SerializedName("email")
    val email: String
)
