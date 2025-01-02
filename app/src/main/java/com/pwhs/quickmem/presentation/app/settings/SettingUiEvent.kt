package com.pwhs.quickmem.presentation.app.settings

sealed class SettingUiEvent {
    data object NavigateToLogin : SettingUiEvent()
    data object NavigateToChangeFullName : SettingUiEvent()
    data object NavigateToChangeEmail : SettingUiEvent()
    data object NavigateToChangeUsername : SettingUiEvent()
    data object NavigateToChangeRole : SettingUiEvent()
    data class ShowError(val error: Int) : SettingUiEvent()
}