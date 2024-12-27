package com.pwhs.quickmem.presentation.app.settings.user_info.full_name

import kotlinx.serialization.Serializable

@Serializable
data class UpdateFullNameArgs(
    val userId: String,
    val fullName: String,
)
