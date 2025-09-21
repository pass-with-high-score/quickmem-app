package com.pwhs.quickmem.presentation.auth.update_fullname

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.presentation.auth.component.AuthButton
import com.pwhs.quickmem.presentation.auth.component.AuthTextField
import com.pwhs.quickmem.presentation.components.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.utils.gradientBackground
import com.pwhs.quickmem.utils.rememberImeState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.generated.destinations.UpdateFullNameScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun UpdateFullNameScreen(
    modifier: Modifier = Modifier,
    viewModel: UpdateFullNameViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UpdateFullNameUiEvent.UpdateSuccess -> {
                    navigator.navigate(HomeScreenDestination()) {
                        popUpTo(UpdateFullNameScreenDestination) {
                            inclusive = true
                        }
                    }
                }

                is UpdateFullNameUiEvent.ShowError -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_error_occurred),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    UpdateFullName(
        modifier,
        fullName = uiState.fullName,
        isLoading = uiState.isLoading,
        onNameChanged = { name ->
            viewModel.onEvent(UpdateFullNameUiAction.FullNameChanged(name))
        },
        onSubmitClick = {
            viewModel.onEvent(UpdateFullNameUiAction.Submit)
        },
        onSkipClick = {
            navigator.navigate(HomeScreenDestination()) {
                popUpTo(HomeScreenDestination) {
                    inclusive = true
                }
            }
        },
        errorMessage = uiState.errorMessage
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateFullName(
    modifier: Modifier = Modifier,
    fullName: String = "",
    onNameChanged: (String) -> Unit = {},
    onSubmitClick: () -> Unit = {},
    onSkipClick: () -> Unit = {},
    isLoading: Boolean = false,
    @StringRes errorMessage: Int? = null,
) {
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()
    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue, tween(300))
        }
    }
    Scaffold(
        modifier = modifier.gradientBackground(),
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = Color.Transparent,
                ),
                title = {
                    Text(
                        text = stringResource(R.string.txt_what_should_we_call_you),
                        style = typography.titleMedium.copy(
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    )
                },
                actions = {
                    TextButton(
                        onClick = onSkipClick
                    ) {
                        IconButton(onClick = onSkipClick) {
                            Icon(
                                Icons.Filled.Close,
                                tint = Color.White,
                                contentDescription = stringResource(R.string.txt_close),
                            )
                        }
                    }
                }
            )
        }
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .verticalScroll(scrollState)
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AuthTextField(
                    value = fullName,
                    onValueChange = onNameChanged,
                    label = stringResource(R.string.txt_full_name),
                    iconId = R.drawable.ic_person,
                    contentDescription = stringResource(R.string.txt_full_name),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 40.dp)
                        .padding(vertical = 16.dp),
                    imeAction = ImeAction.Done,
                    onDone = onSubmitClick,
                    error = errorMessage
                )
                AuthButton(
                    text = stringResource(R.string.txt_submit),
                    onClick = onSubmitClick,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(48.dp)
                )
            }

            LoadingOverlay(
                isLoading = isLoading,
                modifier = Modifier.fillMaxSize(),
                text = stringResource(R.string.txt_updating),
            )
        }
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
fun PreviewSetName() {
    QuickMemTheme {
        UpdateFullName()
    }
}


