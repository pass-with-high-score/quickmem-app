package com.pwhs.quickmem.presentation.auth.forgot_password.send_email

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.TextFieldType
import com.pwhs.quickmem.presentation.auth.component.AuthButton
import com.pwhs.quickmem.presentation.auth.component.AuthTextField
import com.pwhs.quickmem.presentation.auth.component.AuthTopAppBar
import com.pwhs.quickmem.presentation.component.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.util.gradientBackground
import com.pwhs.quickmem.util.rememberImeState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.VerifyEmailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination<RootGraph>
fun SendVerifyEmailScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: SendVerifyEmailViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                SendVerifyEmailUiEvent.SendEmailSuccess -> {
                    navigator.navigate(
                        VerifyEmailScreenDestination(
                            email = uiState.value.email,
                            isFromSignup = false,
                            resetPasswordToken = uiState.value.resetPasswordToken
                        )
                    )
                }

                is SendVerifyEmailUiEvent.SendEmailFailure -> {
                    Toast.makeText(context, context.getString(event.message), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    SendVerifyEmail(
        modifier,
        isLoading = uiState.value.isLoading,
        onNavigationIconClick = {
            navigator.navigateUp()
        },
        email = uiState.value.email,
        emailError = uiState.value.emailError,
        onEmailChanged = { email ->
            viewModel.onEvent(SendVerifyEmailUiAction.EmailChangedAction(email))
        },
        onResetClick = {
            viewModel.onEvent(SendVerifyEmailUiAction.ResetPassword)
        }
    )
}

@Composable
private fun SendVerifyEmail(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onNavigationIconClick: () -> Unit = {},
    email: String = "",
    @StringRes emailError: Int? = null,
    onEmailChanged: (String) -> Unit = {},
    onResetClick: () -> Unit = {},
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
            AuthTopAppBar(onClick = onNavigationIconClick)
        }
    ) { innerPadding ->
        Box {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState)
                    .padding(top = 40.dp)
                    .imePadding(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(26.dp))
                Text(
                    text = stringResource(id = R.string.txt_forgot_your_password),
                    style = typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = colorScheme.primary
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                AuthTextField(
                    value = email,
                    onValueChange = onEmailChanged,
                    label = stringResource(id = R.string.txt_email_address),
                    iconId = R.drawable.ic_email,
                    contentDescription = stringResource(id = R.string.txt_email),
                    type = TextFieldType.EMAIL,
                    error = emailError,
                    imeAction = ImeAction.Done,
                    onDone = onResetClick
                )

                AuthButton(
                    text = stringResource(id = R.string.txt_reset_password),
                    onClick = onResetClick,
                    modifier = Modifier.padding(top = 16.dp),
                    textColor = Color.White,
                )
            }
            LoadingOverlay(
                isLoading = isLoading
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewForgotPasswordVerifyEmailScreen() {
    QuickMemTheme {
        SendVerifyEmail()
    }
}
