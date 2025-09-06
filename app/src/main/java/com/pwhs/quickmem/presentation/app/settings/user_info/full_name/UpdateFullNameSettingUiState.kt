package com.pwhs.quickmem.presentation.app.settings.user_info.full_name

data class UpdateFullNameSettingUiState(
    val fullName: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
