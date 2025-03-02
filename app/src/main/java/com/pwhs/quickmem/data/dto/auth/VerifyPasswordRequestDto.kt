package com.pwhs.quickmem.data.dto.auth

import com.google.gson.annotations.SerializedName

data class VerifyPasswordRequestDto(
    @SerializedName("password")
    val password: String
)
