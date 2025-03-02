package com.pwhs.quickmem.data.dto.auth

import com.google.gson.annotations.SerializedName

data class UpdateUsernameRequestDto(
    @SerializedName("newUsername")
    val newUsername: String,
)