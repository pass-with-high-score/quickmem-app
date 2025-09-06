package com.pwhs.quickmem.data.dto.user

import kotlinx.serialization.SerialName
import com.pwhs.quickmem.data.dto.folder.GetFolderResponseDto
import com.pwhs.quickmem.data.dto.study_set.GetStudySetResponseDto
import kotlinx.serialization.Serializable

@Serializable
data class UserDetailResponseDto(
    @SerialName("avatarUrl")
    val avatarUrl: String,
    @SerialName("folders")
    val folders: List<GetFolderResponseDto>,
    @SerialName("fullname")
    val fullName: String,
    @SerialName("id")
    val id: String,
    @SerialName("studySets")
    val studySets: List<GetStudySetResponseDto>,
    @SerialName("username")
    val username: String
)