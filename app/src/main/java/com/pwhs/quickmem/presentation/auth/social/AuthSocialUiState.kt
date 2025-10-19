package com.pwhs.quickmem.presentation.auth.social

import androidx.annotation.StringRes
import com.pwhs.quickmem.core.data.enums.AuthProvider

data class AuthSocialUiState(
    val isLoading: Boolean = false,
    @param:StringRes val error: Int? = null,
    val provider: AuthProvider? = null,
    val email: String = "",
    val birthDay: String = "",
    @param:StringRes val birthdayError: Int? = null,
    val avatarUrl: String = "",
    val fullName: String = "",
    val token: String = "",
    val id: String = "",
)