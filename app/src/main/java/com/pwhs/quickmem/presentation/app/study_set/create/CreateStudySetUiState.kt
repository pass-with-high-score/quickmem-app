package com.pwhs.quickmem.presentation.app.study_set.create

import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.domain.model.subject.SubjectModel

data class CreateStudySetUiState(
    val title: String = "",
    val titleError: String = "",
    val subjectModel: SubjectModel = SubjectModel.defaultSubjects.first(),
    val subjectError: String = "",
    val colorModel: ColorModel = ColorModel.defaultColors.first(),
    val colorError: String = "",
    val isPublic: Boolean = false,
)