package com.pwhs.quickmem.data.dto.classes

import com.google.gson.annotations.SerializedName

data class JoinClassRequestDto(
    @SerializedName("joinToken")
    val joinToken: String,
    @SerializedName("classId")
    val classId: String
)