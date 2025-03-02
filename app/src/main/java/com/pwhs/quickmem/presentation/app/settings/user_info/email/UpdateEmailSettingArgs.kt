package com.pwhs.quickmem.presentation.app.settings.user_info.email

import kotlinx.serialization.Serializable

@Serializable
data class UpdateEmailSettingArgs(
    val email: String
)
