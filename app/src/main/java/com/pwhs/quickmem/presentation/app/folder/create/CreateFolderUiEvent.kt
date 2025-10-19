package com.pwhs.quickmem.presentation.app.folder.create

import androidx.annotation.StringRes

sealed class CreateFolderUiEvent {
    data class FolderCreated(val id: String) : CreateFolderUiEvent()
    data class ShowError(@param:StringRes val message: Int) : CreateFolderUiEvent()
}