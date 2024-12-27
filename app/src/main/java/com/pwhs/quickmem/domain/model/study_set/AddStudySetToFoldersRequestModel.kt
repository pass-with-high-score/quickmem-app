package com.pwhs.quickmem.domain.model.study_set

data class AddStudySetToFoldersRequestModel(
    val studySetId: String,
    val folderIds: List<String>,
)