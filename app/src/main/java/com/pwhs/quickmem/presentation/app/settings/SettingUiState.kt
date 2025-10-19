package com.pwhs.quickmem.presentation.app.settings

import androidx.annotation.StringRes
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.alarm.StudyAlarm
import com.revenuecat.purchases.CustomerInfo
import java.time.LocalDateTime

data class SettingUiState(
    val canChangeInfo: Boolean = false,
    val userLoginProviders: List<String> = emptyList(),
    val isPlaySound: Boolean = false,
    val password: String = "",
    @param:StringRes val errorMessage: Int? = null,
    val isLoading: Boolean = false,
    val showBottomSheet: Boolean = false,
    val userId: String = "",
    val fullName: String = "",
    val username: String = "",
    val email: String = "",
    val changeType: SettingChangeValueEnum = SettingChangeValueEnum.NONE,
    val isPushNotificationsEnabled: Boolean = false,
    val isAppPushNotificationsEnabled: Boolean = false,
    val customerInfo: CustomerInfo? = null,
    val studyAlarm: StudyAlarm = StudyAlarm(
        time = LocalDateTime.now(),
        message = R.string.txt_it_s_time_to_study
    ),
    val isStudyAlarmEnabled: Boolean = false,
    val timeStudyAlarm: String = "",
)
