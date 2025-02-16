package com.pwhs.quickmem.data.dto.verify_email

import com.google.gson.annotations.SerializedName

data class EmailVerificationResponse(
    @SerializedName("input")
    val input: String,
    @SerializedName("is_reachable")
    val isReachable: Boolean,
)
