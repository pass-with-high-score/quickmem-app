package com.pwhs.quickmem.presentation.auth.forgot_password.set_new_password

import androidx.annotation.StringRes

sealed class SetNewPasswordUiEvent {
    data object ResetSuccess : SetNewPasswordUiEvent()
    data class ResetFailure(@param:StringRes val message: Int) : SetNewPasswordUiEvent()
}
