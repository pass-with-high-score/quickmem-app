package com.pwhs.quickmem.presentation.app.folder.edit

import androidx.annotation.StringRes

sealed class EditFolderUiEvent {
    data object FolderEdited : EditFolderUiEvent()
    data class ShowError(@param:StringRes val message: Int) : EditFolderUiEvent()
}