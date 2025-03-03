package com.pwhs.quickmem.data.dto.auth

import com.google.gson.annotations.SerializedName


data class RefreshTokenRequestDto(
    @SerializedName("refreshToken")
    val refreshToken: String
)