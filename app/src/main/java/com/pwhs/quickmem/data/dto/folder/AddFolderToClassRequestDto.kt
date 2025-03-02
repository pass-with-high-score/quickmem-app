package com.pwhs.quickmem.data.dto.folder

import com.google.gson.annotations.SerializedName

data class AddFolderToClassRequestDto (
    @SerializedName("classId")
    val classId: String,
    @SerializedName("folderIds")
    val folderIds: List<String>
)