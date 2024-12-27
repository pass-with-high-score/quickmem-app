package com.pwhs.quickmem.domain.model.classes

data class JoinClassRequestModel(
    val joinToken: String,
    val userId: String,
    val classId: String,
)