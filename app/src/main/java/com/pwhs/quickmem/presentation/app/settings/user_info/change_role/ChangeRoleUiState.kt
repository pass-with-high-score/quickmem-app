package com.pwhs.quickmem.presentation.app.settings.user_info.change_role

import com.pwhs.quickmem.core.data.enums.UserRole

data class ChangeRoleUiState(
    val isLoading: Boolean = false,
    val role: UserRole = UserRole.STUDENT,
    val errorMessage: String? = null,
    val birthday: String = ""
)
