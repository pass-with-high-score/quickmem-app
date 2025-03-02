package com.pwhs.quickmem.data.dto.auth

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequestDto(
    @SerializedName("oldPassword")
    val oldPassword: String,
    @SerializedName("newPassword")
    val newPassword: String,
)