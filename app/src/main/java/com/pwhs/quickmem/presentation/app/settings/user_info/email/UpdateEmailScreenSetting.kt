package com.pwhs.quickmem.presentation.app.settings.user_info.email

import android.widget.Toast
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.presentation.app.settings.component.SettingTextField
import com.pwhs.quickmem.presentation.app.settings.component.SettingTopAppBar
import com.pwhs.quickmem.presentation.component.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination<RootGraph>(navArgs = UpdateEmailSettingArgs::class)
@Composable
fun UpdateEmailSettingScreen(
    modifier: Modifier = Modifier,
    viewModel: UpdateEmailSettingViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UpdateEmailSettingUiEvent.OnError -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }

                UpdateEmailSettingUiEvent.OnEmailChanged -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_check_your_email_for_verification),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    resultNavigator.navigateBack(true)
                }
            }
        }
    }

    UpdateEmailSetting(
        modifier = modifier,
        email = uiState.email,
        errorMessage = uiState.errorMessage,
        isLoading = uiState.isLoading,
        onEmailChanged = { email ->
            viewModel.onEvent(UpdateEmailSettingUiAction.OnEmailChanged(email))
        },
        onNavigateBack = {
            resultNavigator.navigateBack(false)
        },
        onSaved = {
            viewModel.onEvent(UpdateEmailSettingUiAction.OnSaveClicked)
        }
    )
}

@Composable
fun UpdateEmailSetting(
    modifier: Modifier = Modifier,
    email: String = "",
    errorMessage: String = "",
    isLoading: Boolean = false,
    onEmailChanged: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onSaved: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SettingTopAppBar(
                title = stringResource(R.string.txt_email),
                onNavigateBack = onNavigateBack,
                onSaved = onSaved,
                enabled = email.isNotEmpty()
            )
        }
    ) { innerPadding ->
        Box {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                SettingTextField(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .padding(horizontal = 16.dp),
                    value = email,
                    onValueChange = onEmailChanged,
                    placeholder = stringResource(R.string.txt_email),
                    errorMessage = errorMessage
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
private fun UpdateEmailSettingScreenPreview() {
    QuickMemTheme {
        UpdateEmailSetting()
    }
}
