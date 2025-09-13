package com.pwhs.quickmem.domain.model.study_set

import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.domain.model.subject.SubjectModel

data class CreateStudySetResponseModel(
    val id: String,
    val color: ColorModel,
    val description: String,
    val isPublic: Boolean,
    val subject: SubjectModel,
    val isAIGenerated: Boolean,
    val title: String,
    val updatedAt: String,
    val createdAt: String,
)