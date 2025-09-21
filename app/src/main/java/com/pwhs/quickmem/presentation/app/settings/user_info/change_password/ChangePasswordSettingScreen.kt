package com.pwhs.quickmem.presentation.app.settings.user_info.change_password

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.presentation.app.settings.component.SettingTextField
import com.pwhs.quickmem.presentation.app.settings.component.SettingTopAppBar
import com.pwhs.quickmem.presentation.components.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination<RootGraph>
@Composable
fun ChangePasswordSettingScreen(
    modifier: Modifier = Modifier,
    viewModel: ChangePasswordSettingViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ChangePasswordSettingUiEvent.OnError -> {
                    Toast.makeText(
                        context,
                        context.getString(event.errorMessage),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                ChangePasswordSettingUiEvent.OnPasswordChanged -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_password_changed_successfully),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    resultNavigator.navigateBack(true)
                }
            }
        }
    }

    ChangePasswordSetting(
        modifier = modifier,
        currentPassword = uiState.currentPassword,
        newPassword = uiState.newPassword,
        confirmPassword = uiState.confirmPassword,
        errorCurrentPassword = uiState.errorCurrentPassword,
        errorNewPassword = uiState.errorNewPassword,
        errorConfirmPassword = uiState.errorConfirmPassword,
        isLoading = uiState.isLoading,
        onCurrentPasswordChanged = { password ->
            viewModel.onEvent(ChangePasswordSettingUiAction.OnCurrentPasswordChanged(password))
        },
        onNewPasswordChanged = { password ->
            viewModel.onEvent(ChangePasswordSettingUiAction.OnNewPasswordChanged(password))
        },
        onConfirmPasswordChanged = { password ->
            viewModel.onEvent(ChangePasswordSettingUiAction.OnConfirmPasswordChanged(password))
        },
        onNavigateBack = {
            resultNavigator.navigateBack(false)
        },
        onSaved = {
            viewModel.onEvent(ChangePasswordSettingUiAction.OnSaveClicked)
        }
    )
}

@Composable
fun ChangePasswordSetting(
    modifier: Modifier = Modifier,
    currentPassword: String = "",
    newPassword: String = "",
    confirmPassword: String = "",
    @StringRes errorCurrentPassword: Int? = null,
    @StringRes errorNewPassword: Int? = null,
    @StringRes errorConfirmPassword: Int? = null,
    isLoading: Boolean = false,
    onCurrentPasswordChanged: (String) -> Unit = {},
    onNewPasswordChanged: (String) -> Unit = {},
    onConfirmPasswordChanged: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onSaved: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SettingTopAppBar(
                title = stringResource(R.string.txt_change_password),
                onNavigateBack = onNavigateBack,
                onSaved = onSaved,
                enabled = currentPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty()
            )
        }
    ) { innerPadding ->
        Box {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                SettingTextField(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .padding(horizontal = 16.dp),
                    value = currentPassword,
                    onValueChange = onCurrentPasswordChanged,
                    placeholder = stringResource(R.string.txt_current_password),
                    errorMessage = errorCurrentPassword?.let { stringResource(it) },
                    isSecure = true
                )
                SettingTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    value = newPassword,
                    onValueChange = onNewPasswordChanged,
                    placeholder = stringResource(R.string.txt_new_password),
                    errorMessage = errorNewPassword?.let { stringResource(it) },
                    isSecure = true
                )
                SettingTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChanged,
                    placeholder = stringResource(R.string.txt_confirm_new_password),
                    errorMessage = errorConfirmPassword?.let { stringResource(it) },
                    isSecure = true
                )
            }
            LoadingOverlay(
                isLoading = isLoading
            )
        }
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
private fun ChangePasswordSettingScreenPreview() {
    QuickMemTheme {
        ChangePasswordSetting()
    }
}
