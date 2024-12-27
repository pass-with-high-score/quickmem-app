package com.pwhs.quickmem.domain.model.study_set

data class AddStudySetToFolderRequestModel(
    val folderId: String,
    val studySetIds: List<String>,
)