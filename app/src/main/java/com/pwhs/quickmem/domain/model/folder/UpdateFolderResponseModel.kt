package com.pwhs.quickmem.domain.model.folder

import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel

data class UpdateFolderResponseModel(
    val id: String,
    val title: String,
    val description: String,
    val isPublic: Boolean,
    val studySetCount: Int,
    val linkShareCode: String? = null,
    val studySets: List<GetStudySetResponseModel>,
    val updatedAt: String,
    val createdAt: String,
)