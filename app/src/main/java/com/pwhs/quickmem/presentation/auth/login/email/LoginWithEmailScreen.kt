package com.pwhs.quickmem.presentation.auth.login.email

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.TextFieldType
import com.pwhs.quickmem.presentation.auth.component.AuthButton
import com.pwhs.quickmem.presentation.auth.component.AuthTextField
import com.pwhs.quickmem.presentation.auth.component.AuthTopAppBar
import com.pwhs.quickmem.presentation.component.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.utils.gradientBackground
import com.pwhs.quickmem.utils.rememberImeState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SendVerifyEmailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination<RootGraph>(
    navArgs = LoginWithEmailArgs::class
)
fun LoginWithEmailScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: LoginWithEmailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                LoginWithEmailUiEvent.LoginSuccess -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_login_success), Toast.LENGTH_SHORT
                    ).show()
                    navigator.navigate(HomeScreenDestination()) {
                        popUpTo(NavGraphs.root) {
                            saveState = false
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                }

                LoginWithEmailUiEvent.NavigateToVerifyEmail -> {
                    navigator.navigate(
                        SendVerifyEmailScreenDestination()
                    )
                }

                is LoginWithEmailUiEvent.LoginFailure -> {
                    Toast.makeText(
                        context,
                        context.getString(event.message), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    LoginWithEmail(
        modifier = modifier,
        isLoading = uiState.isLoading,
        onNavigationIconClick = { navigator.popBackStack() },
        email = uiState.email,
        emailError = uiState.emailError,
        onEmailChanged = { email -> viewModel.onEvent(LoginWithEmailUiAction.EmailChanged(email)) },
        password = uiState.password,
        passwordError = uiState.passwordError,
        onPasswordChanged = { password ->
            viewModel.onEvent(LoginWithEmailUiAction.PasswordChanged(password))
        },
        onLoginClick = { viewModel.onEvent(LoginWithEmailUiAction.Login) },
        onForgotPasswordClick = {
            navigator.navigate(SendVerifyEmailScreenDestination)
        }
    )
}

@Composable
private fun LoginWithEmail(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onNavigationIconClick: () -> Unit = {},
    email: String = "",
    @StringRes emailError: Int? = null,
    onEmailChanged: (String) -> Unit = {},
    password: String = "",
    @StringRes passwordError: Int? = null,
    onPasswordChanged: (String) -> Unit = {},
    onLoginClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
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
                    .imePadding(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(R.string.txt_login_with_email),
                    style = typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                AuthTextField(
                    value = email,
                    onValueChange = onEmailChanged,
                    label = stringResource(R.string.txt_email),
                    iconId = R.drawable.ic_email,
                    contentDescription = stringResource(R.string.txt_email),
                    type = TextFieldType.EMAIL,
                    error = emailError
                )

                AuthTextField(
                    value = password,
                    onValueChange = onPasswordChanged,
                    label = stringResource(R.string.txt_password),
                    iconId = R.drawable.ic_lock,
                    contentDescription = stringResource(R.string.txt_password),
                    type = TextFieldType.PASSWORD,
                    error = passwordError,
                    imeAction = ImeAction.Done,
                    onDone = onLoginClick
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = onForgotPasswordClick
                    ) {
                        Text(
                            text = stringResource(R.string.txt_forgot_password),
                            style = typography.bodyLarge.copy(
                                color = colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,
                                textDecoration = TextDecoration.Underline
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                AuthButton(
                    text = stringResource(R.string.txt_log_in),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 18.dp),
                    onClick = {
                        onLoginClick()
                    }
                )
            }

            LoadingOverlay(
                isLoading = isLoading,
                modifier = Modifier.fillMaxSize(),
                text = stringResource(R.string.txt_logging_in)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
fun PreviewLoginWithEmailScreen() {
    QuickMemTheme {
        LoginWithEmail()
    }
}
