package com.pwhs.quickmem.presentation.app.classes.create

data class CreateClassUiState(
    val isLoading: Boolean = false,
    val title: String = "",
    val titleError: String = "",
    val description: String = "",
    val allowMemberManagement: Boolean = false,
    val allowSetManagement: Boolean = false,
)