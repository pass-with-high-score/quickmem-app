package com.pwhs.quickmem.data.dto.classes

import com.google.gson.annotations.SerializedName

data class CreateClassRequestDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("allowSetManagement")
    val allowSetManagement: Boolean,
    @SerializedName("allowMemberManagement")
    val allowMemberManagement: Boolean
)
