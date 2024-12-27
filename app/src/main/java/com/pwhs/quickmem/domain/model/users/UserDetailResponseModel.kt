package com.pwhs.quickmem.domain.model.users

import com.pwhs.quickmem.domain.model.classes.GetClassByOwnerResponseModel
import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel

data class UserDetailResponseModel(
    val avatarUrl: String,
    val classes: List<GetClassByOwnerResponseModel>,
    val folders: List<GetFolderResponseModel>,
    val fullName: String,
    val id: String,
    val role: String,
    val studySets: List<GetStudySetResponseModel>,
    val username: String,
)