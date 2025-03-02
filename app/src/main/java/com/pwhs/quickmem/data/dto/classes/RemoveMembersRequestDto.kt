package com.pwhs.quickmem.data.dto.classes

import com.google.gson.annotations.SerializedName

data class RemoveMembersRequestDto(
    @SerializedName("classId")
    val classId: String,
    @SerializedName("memberIds")
    val memberIds: List<String>
)