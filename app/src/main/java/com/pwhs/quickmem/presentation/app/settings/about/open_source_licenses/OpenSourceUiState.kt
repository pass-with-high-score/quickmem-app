package com.pwhs.quickmem.presentation.app.settings.about.open_source_licenses

import com.pwhs.quickmem.presentation.app.settings.about.open_source_licenses.data.LicensesModel

data class OpenSourceUiState(
    val isLoading: Boolean = false,
    val licenses: List<LicensesModel> = emptyList(),
    val errorMessage: String? = null,
)