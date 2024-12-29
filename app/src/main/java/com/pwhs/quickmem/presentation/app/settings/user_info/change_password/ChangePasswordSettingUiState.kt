package com.pwhs.quickmem.presentation.app.settings.user_info.change_password

import androidx.annotation.StringRes

data class ChangePasswordSettingUiState(
    val email: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    @StringRes val errorCurrentPassword: Int? = null,
    @StringRes val errorNewPassword: Int? = null,
    @StringRes val errorConfirmPassword: Int? = null,
    val isLoading: Boolean = false,
)
