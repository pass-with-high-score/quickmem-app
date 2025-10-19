package com.pwhs.quickmem.presentation.auth.signup.email

import androidx.annotation.StringRes

data class SignUpWithEmailUiState(
    val email: String = "",
    @param:StringRes val emailError: Int? = null,
    val password: String = "",
    @param:StringRes val passwordError: Int? = null,
    val birthday: String = "",
    @param:StringRes val birthdayError: Int? = null,
    val isLoading: Boolean = false,
)