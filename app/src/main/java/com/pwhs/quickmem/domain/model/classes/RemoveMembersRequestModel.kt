package com.pwhs.quickmem.domain.model.classes

data class RemoveMembersRequestModel(
    val classId: String,
    val memberIds: List<String>,
)