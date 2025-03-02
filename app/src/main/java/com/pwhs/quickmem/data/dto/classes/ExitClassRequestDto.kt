package com.pwhs.quickmem.data.dto.classes

import com.google.gson.annotations.SerializedName

data class ExitClassRequestDto(
    @SerializedName("classId")
    val classId: String
)