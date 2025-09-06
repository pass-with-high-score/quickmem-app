package com.pwhs.quickmem.data.dto.study_set

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddStudySetToFoldersRequestDto (
    @SerialName("studySetId")
    val studySetId: String,
    @SerialName("folderIds")
    val folderIds: List<String>
)