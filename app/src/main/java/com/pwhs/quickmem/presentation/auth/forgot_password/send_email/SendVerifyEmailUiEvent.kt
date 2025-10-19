package com.pwhs.quickmem.presentation.auth.forgot_password.send_email

import androidx.annotation.StringRes

sealed class SendVerifyEmailUiEvent {
    data object SendEmailSuccess : SendVerifyEmailUiEvent()
    data class SendEmailFailure(@param:StringRes val message: Int) : SendVerifyEmailUiEvent()
}
