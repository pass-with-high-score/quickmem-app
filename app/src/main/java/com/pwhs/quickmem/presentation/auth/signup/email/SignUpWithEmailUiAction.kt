package com.pwhs.quickmem.presentation.auth.signup.email

sealed class SignUpWithEmailUiAction {
    data class EmailChanged(val email: String) : SignUpWithEmailUiAction()
    data class PasswordChanged(val password: String) : SignUpWithEmailUiAction()
    data class BirthdayChanged(val birthday: String) : SignUpWithEmailUiAction()
    data object SignUp : SignUpWithEmailUiAction()
}