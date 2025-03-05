package com.pwhs.quickmem.data.dto.auth

import com.google.gson.annotations.SerializedName

data class SignupRequestDto(
    @SerializedName("avatarUrl")
    val avatarUrl: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("fullName")
    val fullName: String?,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("provider")
    val provider: String? = null,
)