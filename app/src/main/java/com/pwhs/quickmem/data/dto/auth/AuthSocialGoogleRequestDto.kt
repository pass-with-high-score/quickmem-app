package com.pwhs.quickmem.data.dto.auth

import com.google.gson.annotations.SerializedName

data class AuthSocialGoogleRequestDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("provider")
    val provider: String,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("photoUrl")
    val photoUrl: String,
    @SerializedName("idToken")
    val idToken: String,
)