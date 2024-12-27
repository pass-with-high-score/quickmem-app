package com.pwhs.quickmem.domain.model.classes

data class CreateClassResponseModel(
    val id: String,
    val title: String,
    val description: String,
    val joinToken: String,
    val allowMemberManagement: Boolean,
    val allowSetManagement: Boolean,
    val createdAt: String,
    val updatedAt: String,
)
