package com.pwhs.quickmem.domain.model.classes

import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.domain.model.users.ClassMemberModel
import com.pwhs.quickmem.domain.model.users.UserResponseModel

data class GetClassDetailResponseModel(
    val id: String,
    val title: String,
    val description: String,
    val owner: UserResponseModel,
    val isJoined: Boolean? = false,
    val joinToken: String,
    val createdAt: String,
    val updatedAt: String,
    val allowMemberManagement: Boolean,
    val allowSetManagement: Boolean,
    val folderCount: Int,
    val folders: List<GetFolderResponseModel>? = emptyList(),
    val memberCount: Int,
    val members: List<ClassMemberModel>? = emptyList(),
    val studySetCount: Int,
    val studySets: List<GetStudySetResponseModel>? = emptyList(),
)
