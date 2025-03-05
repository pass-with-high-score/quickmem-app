package com.pwhs.quickmem.data.dto.user

import com.google.gson.annotations.SerializedName
import com.pwhs.quickmem.data.dto.folder.GetFolderResponseDto
import com.pwhs.quickmem.data.dto.study_set.GetStudySetResponseDto

data class UserDetailResponseDto(
    @SerializedName("avatarUrl")
    val avatarUrl: String,
    @SerializedName("folders")
    val folders: List<GetFolderResponseDto>,
    @SerializedName("fullname")
    val fullName: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("studySets")
    val studySets: List<GetStudySetResponseDto>,
    @SerializedName("username")
    val username: String
)