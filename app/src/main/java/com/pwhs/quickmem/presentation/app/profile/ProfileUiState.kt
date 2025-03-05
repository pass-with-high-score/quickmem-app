package com.pwhs.quickmem.presentation.app.profile

import com.pwhs.quickmem.domain.model.study_time.GetStudyTimeByUserResponseModel
import com.revenuecat.purchases.CustomerInfo

data class ProfileUiState(
    val userAvatar: String = "",
    val fullName: String = "",
    val username: String = "",
    val customerInfo: CustomerInfo? = null,
    val isLoading: Boolean = false,
    val studyTime: GetStudyTimeByUserResponseModel? = null,
    val createDate: String? = null,
    val coins: Int = 0,
    val studySetCount: Int = 0,
    val folderCount: Int = 0,
    val streakCount: Int = 0,
)