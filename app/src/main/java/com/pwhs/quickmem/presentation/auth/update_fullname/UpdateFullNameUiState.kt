package com.pwhs.quickmem.presentation.auth.update_fullname

import androidx.annotation.StringRes

data class UpdateFullNameUiState(
    val fullName: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    @param:StringRes val errorMessage: Int? = null
)