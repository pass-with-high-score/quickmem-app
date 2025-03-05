package com.pwhs.quickmem.data.dto.auth

import com.google.gson.annotations.SerializedName

data class SignupSocialCredentialRequestDto(
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("idToken")
    val idToken: String,
    @SerializedName("photoUrl")
    val photoUrl: String,
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("provider")
    val provider: String,
    @SerializedName("displayName")
    val displayName: String,
)