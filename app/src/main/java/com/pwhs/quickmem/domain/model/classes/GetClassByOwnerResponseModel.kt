package com.pwhs.quickmem.domain.model.classes

import com.pwhs.quickmem.domain.model.users.UserResponseModel

data class GetClassByOwnerResponseModel(
    val allowMemberManagement: Boolean,
    val allowSetManagement: Boolean,
    val createdAt: String,
    val description: String,
    val folderCount: Int,
    val id: String,
    val joinToken: String?,
    val memberCount: Int,
    val owner: UserResponseModel,
    val studySetCount: Int,
    val title: String,
    val isImported: Boolean? = null,
    val updatedAt: String,
)
