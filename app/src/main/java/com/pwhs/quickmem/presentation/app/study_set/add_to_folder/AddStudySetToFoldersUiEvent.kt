package com.pwhs.quickmem.presentation.app.study_set.add_to_folder

import androidx.annotation.StringRes

sealed class AddStudySetToFoldersUiEvent {
    data object StudySetAddedToFolders : AddStudySetToFoldersUiEvent()
    data class Error(@param:StringRes val message: Int) : AddStudySetToFoldersUiEvent()
}