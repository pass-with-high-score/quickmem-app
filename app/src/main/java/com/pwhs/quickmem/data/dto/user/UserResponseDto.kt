package com.pwhs.quickmem.data.dto.user

import com.google.gson.annotations.SerializedName

data class UserResponseDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("avatarUrl")
    val avatarUrl: String,
    @SerializedName("username")
    val username: String
)