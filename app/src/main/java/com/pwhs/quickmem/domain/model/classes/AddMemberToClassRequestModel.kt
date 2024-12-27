package com.pwhs.quickmem.domain.model.classes

data class AddMemberToClassRequestModel(
    val joinToken: String,
    val userId: String,
    val classId: String,
)