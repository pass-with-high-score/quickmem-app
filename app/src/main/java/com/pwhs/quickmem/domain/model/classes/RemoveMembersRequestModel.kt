package com.pwhs.quickmem.domain.model.classes

data class RemoveMembersRequestModel(
    val userId: String,
    val classId: String,
    val memberIds: List<String>,
)