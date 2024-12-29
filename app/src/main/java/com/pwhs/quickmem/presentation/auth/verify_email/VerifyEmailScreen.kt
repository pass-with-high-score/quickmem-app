package com.pwhs.quickmem.presentation.auth.verify_email

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.presentation.auth.component.AuthButton
import com.pwhs.quickmem.presentation.auth.verify_email.components.OtpInputField
import com.pwhs.quickmem.presentation.component.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.util.gradientBackground
import com.pwhs.quickmem.util.rememberImeState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.SetNewPasswordScreenDestination
import com.ramcosta.composedestinations.generated.destinations.UpdateFullNameScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import timber.log.Timber

@Composable
@Destination<RootGraph>(
    navArgs = VerifyEmailArgs::class
)
fun VerifyEmailScreen(
    modifier: Modifier = Modifier,
    viewModel: VerifyEmailViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                VerifyEmailUiEvent.VerifyFailure -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_failed_to_verify_email), Toast.LENGTH_SHORT
                    ).show()
                }

                VerifyEmailUiEvent.VerifySuccess -> {
                    navigator.popBackStack()
                    navigator.navigate(UpdateFullNameScreenDestination) {
                        popUpTo(NavGraphs.root) {
                            saveState = false
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                }

                VerifyEmailUiEvent.ResendFailure -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_failed_to_resend_email), Toast.LENGTH_SHORT
                    ).show()
                }

                VerifyEmailUiEvent.ResendSuccess -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_please_check_your_email_again),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                VerifyEmailUiEvent.WrongOtp -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_otp_must_be_numbers), Toast.LENGTH_SHORT
                    ).show()
                }

                VerifyEmailUiEvent.EmptyOtp -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_otp_must_not_be_empty), Toast.LENGTH_SHORT
                    ).show()
                }

                VerifyEmailUiEvent.ErrorLengthOtp -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_otp_must_be_6_digits), Toast.LENGTH_SHORT
                    ).show()
                }

                VerifyEmailUiEvent.NavigateToSetNewPassword -> {
                    Timber.d(uiState.resetPasswordToken)
                    navigator.navigate(
                        SetNewPasswordScreenDestination(
                            email = uiState.email,
                            resetPasswordToken = uiState.resetPasswordToken,
                            otp = uiState.otp
                        )
                    )
                }
            }

        }
    }
    VerifyEmail(
        modifier = modifier,
        isLoading = uiState.isLoading,
        otp = uiState.otp,
        email = uiState.email,
        countdownTime = uiState.countdown,
        onVerifyClick = {
            viewModel.onEvent(VerifyEmailUiAction.VerifyEmail)
        },
        onOtpChange = {
            viewModel.onEvent(VerifyEmailUiAction.OtpChange(it))
        },
        onResendClick = {
            viewModel.onEvent(VerifyEmailUiAction.ResendEmail)
        },
        onNavigationBack = {
            navigator.navigateUp()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VerifyEmail(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    email: String = "",
    otp: String = "",
    countdownTime: Int = 0,
    onVerifyClick: () -> Unit = {},
    onOtpChange: (String) -> Unit = {},
    onResendClick: () -> Unit = {},
    onNavigationBack: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
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
            TopAppBar(
                title = {},
                colors = topAppBarColors(
                    containerColor = Color.Transparent,
                ),
                navigationIcon = {
                    IconButton(
                        onClick = onNavigationBack
                    ) {
                        Icon(
                            imageVector = AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.txt_back),
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box {
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState)
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.txt_check_your_email),
                        style = typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(R.string.txt_please_enter_the_code_we_sent_to_your_email))
                            withStyle(style = SpanStyle(color = colorScheme.primary)) {
                                append(" $email")
                            }
                        },
                        style = typography.bodyLarge.copy(
                            color = colorScheme.onSurface,
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

                Box(
                    contentAlignment = Alignment.Center
                ) {
                    OtpInputField(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .padding(bottom = 10.dp),
                        otpText = otp,
                        shouldCursorBlink = false,
                        onOtpModified = { value, otpFilled ->
                            onOtpChange(value)
                            if (otpFilled) {
                                keyboardController?.hide()
                            }
                        }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.txt_didn_t_receive_the_code),
                        style = typography.bodyMedium
                    )
                    TextButton(
                        onClick = onNavigationBack,
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.padding(start = 5.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.txt_update_email),
                            style = typography.bodyMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            ),
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }

                AuthButton(
                    text = stringResource(R.string.txt_verify),
                    onClick = onVerifyClick,
                    modifier = Modifier
                        .padding(vertical = 15.dp)
                        .padding(horizontal = 32.dp)
                )

                AnimatedContent(
                    targetState = countdownTime,
                    transitionSpec = {
                        if (targetState > 0) {
                            slideInVertically() togetherWith slideOutVertically()
                        } else {
                            fadeIn() togetherWith fadeOut()
                        }
                    }

                ) { targetCountdown ->
                    if (targetCountdown == 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = stringResource(R.string.txt_resend),
                            )
                            TextButton(
                                onClick = onResendClick,
                            ) {
                                Text(
                                    text = stringResource(R.string.txt_resend),
                                    style = typography.bodyLarge.copy(
                                        color = colorScheme.onSurface,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }

            }
            LoadingOverlay(
                isLoading = isLoading,
                text = stringResource(R.string.txt_verifying)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
fun VerifyEmailScreenPreview() {
    QuickMemTheme {
        VerifyEmail(email = "")
    }
}