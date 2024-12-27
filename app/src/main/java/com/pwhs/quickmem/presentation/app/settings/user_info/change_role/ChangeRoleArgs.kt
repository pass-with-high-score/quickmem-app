package com.pwhs.quickmem.presentation.app.settings.user_info.change_role

import kotlinx.serialization.Serializable

@Serializable
data class ChangeRoleArgs(
    val userId: String,
    val role: String,
)
