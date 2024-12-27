package com.pwhs.quickmem.presentation.app.settings.user_info.change_password

data class ChangePasswordSettingUiState(
    val email: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val errorCurrentPassword: String = "",
    val errorNewPassword: String = "",
    val errorConfirmPassword: String = "",
    val isLoading: Boolean = false,
)
