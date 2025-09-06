package com.pwhs.quickmem.data.dto.study_set

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddStudySetToFolderRequestDto (
    @SerialName("folderId")
    val folderId: String,
    @SerialName("studySetIds")
    val studySetIds: List<String>
)