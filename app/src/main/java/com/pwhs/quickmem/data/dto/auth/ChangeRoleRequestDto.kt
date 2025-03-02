package com.pwhs.quickmem.data.dto.auth

import com.google.gson.annotations.SerializedName

data class ChangeRoleRequestDto(
    @SerializedName("role")
    val role: String,
)
