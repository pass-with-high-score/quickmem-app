package com.pwhs.quickmem.data.dto.user

import com.google.gson.annotations.SerializedName

class SearchUserResponseDto (
    @SerializedName("id")
    val id: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("avatarUrl")
    val avatarUrl: String,
)