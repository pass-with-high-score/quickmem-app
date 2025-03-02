package com.pwhs.quickmem.data.dto.auth

import com.google.gson.annotations.SerializedName

data class UpdateFullNameRequestDto(
    @SerializedName("fullname")
    val fullName: String,
)