package com.pwhs.quickmem.presentation.auth.social

sealed class AuthSocialUiAction {
    data class OnEmailChanged(val email: String) : AuthSocialUiAction()
    data class OnBirthDayChanged(val birthDay: String) : AuthSocialUiAction()
    data class OnAvatarUrlChanged(val avatarUrl: String) : AuthSocialUiAction()
    data class OnNameChanged(val name: String) : AuthSocialUiAction()
    data object Register : AuthSocialUiAction()
}