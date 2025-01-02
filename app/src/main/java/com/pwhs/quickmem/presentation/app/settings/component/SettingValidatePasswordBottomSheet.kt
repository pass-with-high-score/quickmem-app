package com.pwhs.quickmem.presentation.app.settings.component

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.auth.AuthSocialGoogleRequestModel
import com.pwhs.quickmem.presentation.auth.utils.GoogleSignInUtils
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingValidatePasswordBottomSheet(
    modifier: Modifier = Modifier,
    showVerifyPasswordBottomSheet: Boolean = false,
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    password: String = "",
    errorMessage: String? = null,
    onChangePassword: (String) -> Unit = {},
    onSubmitClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    isGoogleSignIn: Boolean = false,
    onVerifyWithGoogle: (AuthSocialGoogleRequestModel) -> Unit = {},
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
                    onVerifyWithGoogle(
                        AuthSocialGoogleRequestModel(
                            id = it.id,
                            email = it.email,
                            provider = it.provider,
                            displayName = it.displayName,
                            photoUrl = it.photoUrl,
                            idToken = it.idToken,
                        )
                    )
                }
            )

        }
    if (showVerifyPasswordBottomSheet) {
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            sheetState = bottomSheetState,
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    stringResource(R.string.txt_first_verify_your_account),
                    style = typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = when {
                        isGoogleSignIn -> stringResource(R.string.txt_to_confirm_auth_google)
                        else -> stringResource(R.string.txt_to_confirm_auth_email)
                    },
                    style = typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                if (!isGoogleSignIn) {
                    SettingTextField(
                        modifier = Modifier.padding(top = 16.dp),
                        value = password,
                        onValueChange = onChangePassword,
                        errorMessage = errorMessage,
                        placeholder = stringResource(R.string.txt_password),
                        isSecure = true
                    )
                    Button(
                        enabled = password.isNotEmpty() && password.length >= 8,
                        onClick = onSubmitClick,
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = shapes.medium
                    ) {
                        Text(
                            stringResource(R.string.txt_submit),
                            style = typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            GoogleSignInUtils.doGoogleSignIn(
                                context = context,
                                scope = scope,
                                launcher = launcher,
                                login = {
                                    onVerifyWithGoogle(
                                        AuthSocialGoogleRequestModel(
                                            id = it.id,
                                            email = it.email,
                                            provider = it.provider,
                                            displayName = it.displayName,
                                            photoUrl = it.photoUrl,
                                            idToken = it.idToken,
                                        )
                                    )
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                        ),
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = shapes.medium
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_google),
                                contentDescription = null,
                            )
                            Text(
                                stringResource(R.string.txt_continue_with_google),
                                style = typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }
                OutlinedButton(
                    onClick = onDismissRequest,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorScheme.onSurface.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    shape = shapes.medium
                ) {
                    Text(
                        stringResource(R.string.txt_cancel),
                        style = typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
private fun SettingValidatePasswordBottomSheetPreview() {
    QuickMemTheme {
        SettingValidatePasswordBottomSheet(
            showVerifyPasswordBottomSheet = true,
            password = "password",
            errorMessage = "Error message",
            onChangePassword = { },
            onSubmitClick = { },
            onDismissRequest = { }
        )
    }
}