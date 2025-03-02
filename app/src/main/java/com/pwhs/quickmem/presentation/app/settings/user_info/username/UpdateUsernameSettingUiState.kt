package com.pwhs.quickmem.presentation.app.settings.user_info.username

data class UpdateUsernameSettingUiState(
    val currentUsername: String = "",
    val newUsername: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)