package com.pwhs.quickmem.presentation.auth.signup

import androidx.annotation.StringRes

data class SignupUiState(
    val isLoading: Boolean = false,
    @param:StringRes val error: Int? = null,
)