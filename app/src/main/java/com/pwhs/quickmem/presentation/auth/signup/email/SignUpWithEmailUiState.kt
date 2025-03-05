package com.pwhs.quickmem.presentation.auth.signup.email

import androidx.annotation.StringRes

data class SignUpWithEmailUiState(
    val email: String = "",
    @StringRes val emailError: Int? = null,
    val password: String = "",
    @StringRes val passwordError: Int? = null,
    val birthday: String = "",
    @StringRes val birthdayError: Int? = null,
    val isLoading: Boolean = false,
)