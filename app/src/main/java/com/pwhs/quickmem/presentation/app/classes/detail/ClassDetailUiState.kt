package com.pwhs.quickmem.presentation.app.classes.detail

import androidx.annotation.StringRes
import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.domain.model.users.ClassMemberModel
import com.pwhs.quickmem.domain.model.users.UserResponseModel

data class ClassDetailUiState(
    val joinClassCode: String = "",
    val isLogin: Boolean = false,
    val isOwner: Boolean = false,
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val isAllowManage: Boolean = false,
    val allowMember: Boolean = false,
    val isMember: Boolean = false,
    val username: String = "",
    @StringRes val errorMessage: Int? = null,
    val isInvited: Boolean = false,
    val userResponseModel: UserResponseModel = UserResponseModel(),
    val studySets: List<GetStudySetResponseModel> = emptyList(),
    val folders: List<GetFolderResponseModel> = emptyList(),
    val members: List<ClassMemberModel> = emptyList(),
)