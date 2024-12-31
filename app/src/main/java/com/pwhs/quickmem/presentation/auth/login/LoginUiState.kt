package com.pwhs.quickmem.presentation.auth.login

import androidx.annotation.StringRes

data class LoginUiState(
    val isLoading: Boolean = false,
    @StringRes val error: Int? = null,
)