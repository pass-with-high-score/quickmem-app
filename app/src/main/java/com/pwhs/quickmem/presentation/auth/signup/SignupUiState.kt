package com.pwhs.quickmem.presentation.auth.signup

import androidx.annotation.StringRes

data class SignupUiState(
    val isLoading: Boolean = false,
    @StringRes val error: Int? = null,
)