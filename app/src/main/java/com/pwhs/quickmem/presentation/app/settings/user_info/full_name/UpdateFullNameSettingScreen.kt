package com.pwhs.quickmem.presentation.app.settings.user_info.full_name

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

@Destination<RootGraph>(
    navArgs = UpdateFullNameArgs::class
)
@Composable
fun UpdateFullNameSettingScreen(
    modifier: Modifier = Modifier,
    viewModel: UpdateFullNameSettingViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UpdateFullNameSettingUiEvent.OnError -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }

                UpdateFullNameSettingUiEvent.OnFullNameChanged -> {
                    resultNavigator.navigateBack(true)
                }
            }
        }
    }
    UpdateFullNameSetting(
        modifier = modifier,
        fullName = uiState.fullName,
        errorMessage = uiState.errorMessage,
        isLoading = uiState.isLoading,
        onFullNameChanged = { fullName ->
            viewModel.onEvent(UpdateFullNameSettingUiAction.OnFullNameChanged(fullName))
        },
        onNavigateBack = {
            resultNavigator.navigateBack(false)
        },
        onSaved = {
            viewModel.onEvent(UpdateFullNameSettingUiAction.OnSaveClicked)
        }
    )
}

@Composable
fun UpdateFullNameSetting(
    modifier: Modifier = Modifier,
    fullName: String = "",
    errorMessage: String = "",
    isLoading: Boolean = false,
    onFullNameChanged: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onSaved: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SettingTopAppBar(
                title = stringResource(R.string.txt_full_name),
                onNavigateBack = onNavigateBack,
                onSaved = onSaved,
                enabled = fullName.isNotEmpty()
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
                    value = fullName,
                    onValueChange = onFullNameChanged,
                    placeholder = stringResource(R.string.txt_full_name),
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
private fun UpdateFullNameSettingScreenPreview() {
    QuickMemTheme {
        UpdateFullNameSetting()
    }
}