package com.pwhs.quickmem.presentation.app.settings.user_info.username

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUsernameArgs(
    val username: String,
)