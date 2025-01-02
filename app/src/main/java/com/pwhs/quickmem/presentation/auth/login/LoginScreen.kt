package com.pwhs.quickmem.presentation.auth.login

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.auth.AuthSocialGoogleRequestModel
import com.pwhs.quickmem.presentation.auth.component.AuthButton
import com.pwhs.quickmem.presentation.auth.component.AuthTopAppBar
import com.pwhs.quickmem.presentation.auth.utils.GoogleSignInUtils
import com.pwhs.quickmem.presentation.component.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.utils.gradientBackground
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.AuthSocialScreenDestination
import com.ramcosta.composedestinations.generated.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.generated.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.generated.destinations.LoginWithEmailScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SignupScreenDestination
import com.ramcosta.composedestinations.generated.destinations.WelcomeScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import androidx.compose.runtime.getValue

@Destination<RootGraph>
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {

                is LoginUiEvent.LoginWithFacebook -> {
                    // open web view
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_currently_not_available),
                        Toast.LENGTH_SHORT
                    ).show()
//                    navigator.navigate(
//                        WebViewAppDestination(
//                            oAuthLink = "https://api.quickmem.app/auth/facebook",
//                        )
//                    )
                }

                is LoginUiEvent.LoginWithEmail -> {
                    navigator.navigate(LoginWithEmailScreenDestination())
                }

                is LoginUiEvent.NavigateToSignUp -> {
                    navigator.navigate(SignupScreenDestination) {
                        popUpTo(LoginScreenDestination) {
                            inclusive = true
                        }
                    }
                }

                is LoginUiEvent.LoginFailure -> {
                    Toast.makeText(
                        context,
                        "Maybe you need to sign up first",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigator.navigate(
                        AuthSocialScreenDestination(
                            email = event.authSocialGoogleRequestModel.email,
                            displayName = event.authSocialGoogleRequestModel.displayName,
                            photoUrl = event.authSocialGoogleRequestModel.photoUrl,
                            idToken = event.authSocialGoogleRequestModel.idToken,
                            id = event.authSocialGoogleRequestModel.id,
                            provider = event.authSocialGoogleRequestModel.provider.toString()
                        )
                    )
                }

                is LoginUiEvent.LoginSuccess -> {
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

                is LoginUiEvent.ShowError -> {
                    Toast.makeText(
                        context,
                        context.getString(event.error),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is LoginUiEvent.NavigateToLoginWithEmail -> {
                    Toast.makeText(
                        context,
                        "Email already exists, please login",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigator.navigate(
                        LoginWithEmailScreenDestination(
                            email = event.email
                        )
                    )
                }
            }
        }
    }

    Login(
        modifier = modifier,
        isLoading = uiState.isLoading,
        onNavigationIconClick = {
            navigator.navigate(WelcomeScreenDestination) {
                popUpTo(LoginScreenDestination) {
                    inclusive = true
                }
            }
        },
        onNavigateToSignup = {
            viewModel.onEvent(LoginUiAction.NavigateToSignUp)
        },
        onLoginWithEmail = {
            viewModel.onEvent(LoginUiAction.LoginWithEmail)
        },
        onLoginWithGoogle = { authSocialGoogleRequestModel ->
            viewModel.onEvent(LoginUiAction.LoginWithGoogle(authSocialGoogleRequestModel))
        },
        onLoginWithFacebook = {
            viewModel.onEvent(LoginUiAction.LoginWithFacebook)
        }
    )

}

@Composable
fun Login(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onNavigationIconClick: () -> Unit = {},
    onNavigateToSignup: () -> Unit = {},
    onLoginWithEmail: () -> Unit = {},
    onLoginWithGoogle: (AuthSocialGoogleRequestModel) -> Unit = {},
    onLoginWithFacebook: () -> Unit = {},
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            GoogleSignInUtils.doGoogleSignIn(
                context = context,
                scope = scope,
                launcher = null,
                login = {
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                }
            )

        }
    Scaffold(
        modifier = modifier.gradientBackground(),
        containerColor = Color.Transparent,
        topBar = {
            AuthTopAppBar(
                onClick = onNavigationIconClick,
                title = {
                    Text(
                        text = stringResource(R.string.txt_log_in),
                        style = typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            )
        }
    ) { innerPadding ->
        Box {
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = stringResource(R.string.txt_login_description),
                    style = typography.bodyLarge.copy(
                        color = colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .padding(bottom = 30.dp),
                )

                AuthButton(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = onLoginWithEmail,
                    text = stringResource(R.string.txt_log_in_with_email),
                    colors = colorScheme.primary,
                    textColor = Color.White,
                    icon = R.drawable.ic_email
                )

                Row(
                    modifier = Modifier.padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        color = colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = stringResource(R.string.txt_or),
                        style = typography.bodyMedium.copy(color = colorScheme.onSurface)
                    )
                    HorizontalDivider(
                        color = colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                }

                AuthButton(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = {
                        GoogleSignInUtils.doGoogleSignIn(
                            context = context,
                            scope = scope,
                            launcher = launcher,
                            login = {
                                onLoginWithGoogle(it)
                            }
                        )
                    },
                    text = stringResource(R.string.txt_continue_with_google),
                    colors = Color.White,
                    textColor = colorScheme.onSurface,
                    icon = R.drawable.ic_google,
                )
                AuthButton(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = onLoginWithFacebook,
                    text = stringResource(R.string.txt_continue_with_facebook),
                    colors = Color.White,
                    textColor = colorScheme.onSurface,
                    icon = R.drawable.ic_facebook
                )

                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = colorScheme.onSurface,
                                fontSize = 16.sp,
                            )
                        ) {
                            append(stringResource(R.string.txt_don_t_have_an_account))
                            withStyle(
                                style = SpanStyle(
                                    color = colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append(" ")
                                append(stringResource(R.string.txt_sign_up))
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable {
                            onNavigateToSignup()
                        }
                )
            }
            LoadingOverlay(isLoading = isLoading)
        }
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
private fun LoginScreenPreview() {
    QuickMemTheme {
        Login()
    }
}
