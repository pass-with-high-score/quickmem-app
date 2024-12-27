package com.pwhs.quickmem.presentation.app.user_detail

import kotlinx.serialization.Serializable

@Serializable
data class UserDetailArgs(
    val userId: String,
)