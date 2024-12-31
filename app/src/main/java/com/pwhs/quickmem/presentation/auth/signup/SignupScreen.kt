package com.pwhs.quickmem.presentation.auth.signup

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.util.gradientBackground
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.AuthSocialScreenDestination
import com.ramcosta.composedestinations.generated.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.generated.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SignupScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SignupWithEmailScreenDestination
import com.ramcosta.composedestinations.generated.destinations.WelcomeScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination<RootGraph>
fun SignupScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: SignupViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SignupUiEvent.SignupWithGoogle -> {
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

                is SignupUiEvent.SignupWithFacebook -> {
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

                is SignupUiEvent.ShowError -> {
                    Toast.makeText(
                        context,
                        context.getString(event.error),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is SignupUiEvent.SignupSuccess -> {
                    navigator.navigate(HomeScreenDestination()) {
                        popUpTo(NavGraphs.root) {
                            saveState = false
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                }
            }
        }
    }

    Signup(
        modifier = modifier,
        onNavigateToLogin = {
            navigator.navigate(LoginScreenDestination) {
                popUpTo(SignupScreenDestination) {
                    inclusive = true
                }
            }
        },
        onSignupWithEmail = {
            navigator.navigate(SignupWithEmailScreenDestination)
        },
        onNavigationIconClick = {
            navigator.navigate(WelcomeScreenDestination) {
                popUpTo(SignupScreenDestination) {
                    inclusive = true
                }
            }
        },
        onSignupWithGoogle = { authSocialGoogleRequestModel ->
            viewModel.signupWithGoogle(authSocialGoogleRequestModel)
        },
        onSignupWithFacebook = {
            viewModel.signupWithFacebook()
        },
        onPrivacyPolicyClick = {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://pass-with-high-score.github.io/quickmem-term-policy/policy")
            )
            context.startActivity(intent)
        }
    )
}

@Composable
fun Signup(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onSignupWithEmail: () -> Unit = {},
    onSignupWithGoogle: (AuthSocialGoogleRequestModel) -> Unit = {},
    onSignupWithFacebook: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
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
                        text = stringResource(R.string.txt_sign_up),
                        style = typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(R.string.txt_signup_description),
                style = typography.bodyLarge.copy(
                    color = colorScheme.onSurface
                ),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(bottom = 30.dp)
            )

            AuthButton(
                modifier = Modifier.padding(top = 16.dp),
                onClick = onSignupWithEmail,
                text = stringResource(R.string.txt_sign_up_with_email),
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
                            onSignupWithGoogle(it)
                        }
                    )
                },
                text = stringResource(R.string.txt_continue_with_google),
                colors = Color.White,
                textColor = colorScheme.onSurface,
                icon = R.drawable.ic_google
            )
            AuthButton(
                modifier = Modifier.padding(top = 16.dp),
                onClick = onSignupWithFacebook,
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
                        append(stringResource(R.string.txt_by_signing_up_you_agree_to_the))
                        withStyle(
                            style = SpanStyle(
                                color = colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append(stringResource(R.string.txt_terms_and_conditions))
                        }
                        append(stringResource(R.string.txt_and_the))
                        withStyle(
                            style = SpanStyle(
                                color = colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append(stringResource(R.string.txt_privacy_policy))
                        }
                        append(stringResource(R.string.txt_of_quickmem))
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable {
                        onPrivacyPolicyClick()
                    },
                textAlign = TextAlign.Center
            )

            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = colorScheme.onSurface,
                            fontSize = 16.sp,
                        )
                    ) {
                        append(stringResource(R.string.txt_already_have_an_account))
                        withStyle(
                            style = SpanStyle(
                                color = colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append(" " + stringResource(R.string.txt_log_in))
                        }
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable {
                        onNavigateToLogin()
                    }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
private fun SignupScreenPreview() {
    QuickMemTheme {
        Signup()
    }
}