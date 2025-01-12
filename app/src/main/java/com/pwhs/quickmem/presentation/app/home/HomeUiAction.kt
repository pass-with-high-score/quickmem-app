package com.pwhs.quickmem.presentation.app.home

import com.revenuecat.purchases.CustomerInfo

sealed class HomeUiAction {
    data class OnChangeAppPushNotifications(val isAppPushNotificationsEnabled: Boolean) :
        HomeUiAction()

    data class OnChangeCustomerInfo(val customerInfo: CustomerInfo) : HomeUiAction()
    data object RefreshHome : HomeUiAction()
    data object UpdateStreak : HomeUiAction()
}