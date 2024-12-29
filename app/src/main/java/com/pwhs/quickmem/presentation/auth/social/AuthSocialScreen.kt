package com.pwhs.quickmem.presentation.auth.social

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.TextFieldType
import com.pwhs.quickmem.core.data.enums.UserRole
import com.pwhs.quickmem.presentation.auth.component.AuthButton
import com.pwhs.quickmem.presentation.auth.component.AuthTextField
import com.pwhs.quickmem.presentation.auth.component.AuthTopAppBar
import com.pwhs.quickmem.presentation.auth.signup.email.component.DatePickerModalInput
import com.pwhs.quickmem.presentation.auth.signup.email.component.RadioGroup
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.util.gradientBackground
import com.pwhs.quickmem.util.isDateSmallerThan
import com.pwhs.quickmem.util.toFormattedString
import com.pwhs.quickmem.util.toTimestamp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.annotation.parameters.DeepLink
import com.ramcosta.composedestinations.generated.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import timber.log.Timber


@Destination<RootGraph>(
    deepLinks = [
        DeepLink(uriPattern = "quickmem://oauth/google/callback?token={token}&email={email}&fullName={fullName}&provider={provider}&picture={picture}&isSignUp={isSignUp}"),
        DeepLink(uriPattern = "quickmem://oauth/facebook/callback?token={token}&email={email}&fullName={fullName}&provider={provider}&picture={picture}&isSignUp={isSignUp}"),
    ]
)
@Composable
fun AuthSocialScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthSocialViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    email: String = "",
    token: String = "",
    fullName: String = "",
    provider: String = "",
    picture: String = "",
    isSignUp: Boolean = false,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(email, token, fullName, picture, provider, isSignUp) {
        viewModel.initDataDeeplink(
            email = email,
            fullName = fullName,
            avatarUrl = picture,
            token = token,
            provider = provider,
            isSignUp = isSignUp
        )
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                AuthSocialUiEvent.SignUpSuccess -> {
                    Timber.d("Sign up success")
                    navigator.popBackStack()
                    navigator.navigate(HomeScreenDestination()) {
                        popUpTo(HomeScreenDestination) {
                            inclusive = true
                            launchSingleTop = true
                        }
                    }
                }

                is AuthSocialUiEvent.SignUpFailure -> {
                    Toast.makeText(context, context.getString(event.message), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    AuthSocial(
        modifier = modifier,
        onNavigationIconClick = {
            navigator.popBackStack()
        },
        onRegisterClick = {
            viewModel.onEvent(AuthSocialUiAction.Register)
        },
        birthday = uiState.birthDay,
        birthdayError = uiState.birthdayError,
        onBirthdayChanged = { birthday ->
            viewModel.onEvent(AuthSocialUiAction.OnBirthDayChanged(birthday))
        },
        onRoleChanged = { role ->
            viewModel.onEvent(AuthSocialUiAction.OnRoleChanged(role))
        },
    )
}

@Composable
fun AuthSocial(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    birthday: String = "",
    @StringRes birthdayError: Int? = null,
    onBirthdayChanged: (String) -> Unit = {},
    onRoleChanged: (UserRole) -> Unit = {},
) {
    var isDatePickerVisible by rememberSaveable { mutableStateOf(false) }
    var isRoleVisible by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = modifier.gradientBackground(),
        containerColor = Color.Transparent,
        topBar = {
            AuthTopAppBar(
                onClick = onNavigationIconClick,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState)
                .padding(bottom = 16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.txt_almost_done),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(R.string.txt_enter_your_birthday_this_won_t_be_visible_to_others),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    color = colorScheme.onSurface,
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
            )

            AuthTextField(
                value = birthday,
                onValueChange = onBirthdayChanged,
                label = stringResource(R.string.txt_select_your_birthday),
                iconId = R.drawable.ic_calendar,
                contentDescription = stringResource(R.string.txt_select_your_birthday),
                readOnly = true,
                enabled = false,
                onClick = { isDatePickerVisible = true },
                type = TextFieldType.BIRTHDAY,
                error = birthdayError
            )

            if (isRoleVisible) {
                RadioGroup(
                    modifier = Modifier.fillMaxWidth(),
                    onRoleChanged = onRoleChanged
                )
            }
            if (isDatePickerVisible) {
                DatePickerModalInput(
                    onDateSelected = {
                        if (it != null) {
                            onBirthdayChanged(it.toFormattedString())
                            isRoleVisible = !it.isDateSmallerThan()
                        }
                        isDatePickerVisible = false
                    },
                    onDismiss = {
                        isDatePickerVisible = false
                    },
                    initialDate = birthday.toTimestamp()
                )
            }

            AuthButton(
                text = stringResource(R.string.txt_sign_up),
                onClick = onRegisterClick,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
fun PreviewSignupWithGoogleScreen() {
    QuickMemTheme {
        AuthSocial()
    }
}