package com.pwhs.quickmem.domain.model.users

import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel

data class UserDetailResponseModel(
    val avatarUrl: String,
    val folders: List<GetFolderResponseModel>,
    val fullName: String,
    val id: String,
    val studySets: List<GetStudySetResponseModel>,
    val username: String,
)