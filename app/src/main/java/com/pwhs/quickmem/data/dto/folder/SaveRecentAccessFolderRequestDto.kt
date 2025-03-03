package com.pwhs.quickmem.data.dto.folder

import com.google.gson.annotations.SerializedName

data class SaveRecentAccessFolderRequestDto (
    @SerializedName("folderId")
    val folderId: String
)