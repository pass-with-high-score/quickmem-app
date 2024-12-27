package com.pwhs.quickmem.domain.model.classes

data class CreateClassRequestModel(
    val ownerId: String,
    val title: String,
    val description: String,
    val allowMemberManagement: Boolean,
    val allowSetManagement: Boolean,
)
