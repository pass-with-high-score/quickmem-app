package com.pwhs.quickmem.presentation.app.settings

import com.pwhs.quickmem.domain.model.auth.AuthSocialGoogleRequestModel

sealed class SettingUiAction {
    data object Logout : SettingUiAction()
    data object Refresh : SettingUiAction()
    data class OnChangePassword(val password: String) : SettingUiAction()
    data object OnSubmitClick : SettingUiAction()
    data class OnChangeCanChangeInfo(val canChangeInfo: Boolean) : SettingUiAction()
    data class OnChangeType(val changeType: SettingChangeValueEnum) : SettingUiAction()
    data class OnChangePushNotifications(val isPushNotificationsEnabled: Boolean) :
        SettingUiAction()

    data class OnChangeAppPushNotifications(val isAppPushNotificationsEnabled: Boolean) :
        SettingUiAction()

    data class OnChangeStudyAlarm(val isStudyAlarmEnabled: Boolean) : SettingUiAction()
    data class OnChangeTimeStudyAlarm(val timeStudyAlarm: String) : SettingUiAction()
    data class OnChangeIsPlaySound(val isPlaySound: Boolean) : SettingUiAction()
    data class OnVerifyWithGoogle(val authSocialGoogleRequestModel: AuthSocialGoogleRequestModel) :
        SettingUiAction()
}