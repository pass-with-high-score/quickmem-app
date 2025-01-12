package com.pwhs.quickmem.presentation.app.home.notification

sealed class NotificationUiAction {
    data class MarkAsRead(val notificationId: String) : NotificationUiAction()
    data object RefreshNotifications : NotificationUiAction()
    data object ClearAllNotifications : NotificationUiAction()
}