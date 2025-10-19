package com.pwhs.quickmem.presentation.app.settings.user_info.change_password

import androidx.annotation.StringRes

data class ChangePasswordSettingUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    @param:StringRes val errorCurrentPassword: Int? = null,
    @param:StringRes val errorNewPassword: Int? = null,
    @param:StringRes val errorConfirmPassword: Int? = null,
    val isLoading: Boolean = false,
)
