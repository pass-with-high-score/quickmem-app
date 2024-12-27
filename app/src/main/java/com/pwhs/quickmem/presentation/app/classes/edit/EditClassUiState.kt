package com.pwhs.quickmem.presentation.app.classes.edit

data class EditClassUiState(
    val id: String = "",
    val isLoading: Boolean = false,
    val allowSetManagement: Boolean = false,
    val allowMemberManagement: Boolean = false,
    val title: String = "",
    val description: String = "",
)