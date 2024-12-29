package com.pwhs.quickmem.presentation.app.settings.user_info.change_password

import androidx.annotation.StringRes

sealed class ChangePasswordSettingUiEvent {
    data class OnError(@StringRes val errorMessage: Int) : ChangePasswordSettingUiEvent()
    data object OnPasswordChanged : ChangePasswordSettingUiEvent()
}