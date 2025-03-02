package com.pwhs.quickmem.data.dto.classes

import com.google.gson.annotations.SerializedName

data class UpdateClassRequestDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("allowMemberManagement")
    val allowMemberManagement: Boolean,
    @SerializedName("allowSetManagement")
    val allowSetManagement: Boolean
)
