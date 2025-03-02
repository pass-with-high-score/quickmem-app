package com.pwhs.quickmem.presentation.app.settings.user_info.full_name

import androidx.annotation.StringRes

data class UpdateFullNameSettingUiState(
    val fullName: String = "",
    val isLoading: Boolean = false,
    @StringRes val errorMessage: String = "",
)