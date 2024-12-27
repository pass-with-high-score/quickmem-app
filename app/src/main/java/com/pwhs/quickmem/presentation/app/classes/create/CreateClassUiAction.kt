package com.pwhs.quickmem.presentation.app.classes.create

sealed class CreateClassUiAction {
    data class TitleChanged(val title: String) : CreateClassUiAction()
    data class DescriptionChanged(val description: String) : CreateClassUiAction()
    data class MemberManagementChanged(val allowMemberManagement: Boolean) :
        CreateClassUiAction()

    data class SetManagementChanged(val allowSetManagement: Boolean) :
        CreateClassUiAction()

    data object SaveClicked : CreateClassUiAction()
}