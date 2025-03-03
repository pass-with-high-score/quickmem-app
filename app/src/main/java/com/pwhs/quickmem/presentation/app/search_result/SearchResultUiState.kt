package com.pwhs.quickmem.presentation.app.search_result

import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.domain.model.subject.SubjectModel
import com.pwhs.quickmem.presentation.app.search_result.study_set.enums.SearchResultCreatorEnum
import com.pwhs.quickmem.presentation.app.search_result.study_set.enums.SearchResultSizeEnum

data class SearchResultUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val title: String = "",
    val isAIGenerated: Boolean = false,
    val subjectModel: SubjectModel = SubjectModel.defaultSubjects.first(),
    val colorModel: ColorModel = ColorModel.defaultColors.first(),
    val sizeStudySetModel: SearchResultSizeEnum = SearchResultSizeEnum.ALL,
    val sizeFolderModel: Int = 3,
    val sizeClassModel: Int = 3,
    val creatorTypeModel: SearchResultCreatorEnum = SearchResultCreatorEnum.ALL,
)
