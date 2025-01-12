package com.pwhs.quickmem.presentation.app.home.notification

import androidx.annotation.StringRes
import com.pwhs.quickmem.domain.model.notification.GetNotificationResponseModel

data class NotificationUiState(
    val isLoading: Boolean = false,
    val notifications: List<GetNotificationResponseModel> = emptyList(),
    @StringRes val errorMessage: Int? = null,
)