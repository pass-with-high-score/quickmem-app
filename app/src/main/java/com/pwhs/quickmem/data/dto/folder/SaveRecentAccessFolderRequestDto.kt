package com.pwhs.quickmem.data.dto.folder

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveRecentAccessFolderRequestDto (
    @SerialName("folderId")
    val folderId: String
)