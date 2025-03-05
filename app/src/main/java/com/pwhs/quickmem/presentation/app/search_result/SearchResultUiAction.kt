package com.pwhs.quickmem.presentation.app.search_result

import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.domain.model.subject.SubjectModel
import com.pwhs.quickmem.presentation.app.search_result.study_set.enums.SearchResultSizeEnum

sealed class SearchResultUiAction() {
    data object Refresh : SearchResultUiAction()
    data object RefreshStudySets : SearchResultUiAction()
    data object RefreshFolders : SearchResultUiAction()
    data object RefreshSearchAllResult : SearchResultUiAction()
    data class SubjectChanged(val subjectModel: SubjectModel) : SearchResultUiAction()
    data class ColorChanged(val colorModel: ColorModel) : SearchResultUiAction()
    data class SizeChanged(val sizeModel: SearchResultSizeEnum) : SearchResultUiAction()
    data class IsAiGeneratedChanged(val isAiGenerated: Boolean) : SearchResultUiAction()
    data object ApplyFilter : SearchResultUiAction()
    data object ResetFilter : SearchResultUiAction()
}
