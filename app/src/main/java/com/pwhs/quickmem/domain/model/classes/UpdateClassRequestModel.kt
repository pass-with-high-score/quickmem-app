package com.pwhs.quickmem.domain.model.classes

data class UpdateClassRequestModel(
    val title: String,
    val description: String,
    val ownerId: String,
    val allowMemberManagement: Boolean,
    val allowSetManagement: Boolean,
)