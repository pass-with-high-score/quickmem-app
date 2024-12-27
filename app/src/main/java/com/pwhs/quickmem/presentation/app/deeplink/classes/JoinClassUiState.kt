package com.pwhs.quickmem.presentation.app.deeplink.classes

import com.pwhs.quickmem.domain.model.classes.GetClassDetailResponseModel

data class JoinClassUiState(
    val isLoading: Boolean = false,
    val userId: String? = null,
    val classId: String? = null,
    val isUnAuthorized: Boolean = false,
    val code: String? = null,
    val isFromDeepLink: Boolean = false,
    val classDetailResponseModel: GetClassDetailResponseModel? = null,
)