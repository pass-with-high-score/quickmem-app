package com.pwhs.quickmem.presentation.auth.social

import androidx.annotation.StringRes
import com.pwhs.quickmem.core.data.enums.AuthProvider
import com.pwhs.quickmem.core.data.enums.UserRole

data class AuthSocialUiState(
    val isLoading: Boolean = false,
    @StringRes val error: Int? = null,
    val provider: AuthProvider? = null,
    val email: String = "",
    val birthDay: String = "",
    @StringRes val birthdayError: Int? = null,
    val avatarUrl: String = "",
    val role: UserRole = UserRole.STUDENT,
    val fullName: String = "",
    val token: String = "",
    val id: String = "",
)