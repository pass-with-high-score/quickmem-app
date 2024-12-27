package com.pwhs.quickmem.presentation.app.settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun SettingTextField(
    modifier: Modifier = Modifier,
    placeholder: String = "",
    value: String = "",
    onValueChange: (String) -> Unit = {},
    errorMessage: String? = null,
    isSecure: Boolean? = null,
) {
    var showPassword by remember {
        mutableStateOf(false)
    }
    TextField(
        modifier = modifier
            .fillMaxWidth(),
        value = value,
        maxLines = 1,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                style = typography.bodyMedium.copy(
                    color = colorScheme.onSurface.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Bold
                )
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            errorContainerColor = Color.White,
        ),
        visualTransformation = if (isSecure == true && !showPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = {
            if (isSecure != null) {
                IconButton(
                    onClick = {
                        showPassword = !showPassword
                    }
                ) {
                    Icon(
                        imageVector = if (showPassword) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        },
                        contentDescription = stringResource(R.string.txt_toggle_password_visibility),
                        tint = colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        },
        shape = shapes.medium,
        isError = errorMessage?.isNotEmpty() == true,
        supportingText = {
            errorMessage?.let {
                Text(
                    text = it,
                    color = colorScheme.error,
                    style = typography.bodyMedium
                )
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun SettingTextFieldPreview() {
    QuickMemTheme {
        Scaffold {
            Column(
                modifier = Modifier.padding(it),
            ) {
                SettingTextField(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .padding(horizontal = 16.dp),
                    placeholder = "Placeholder",
                    value = "Value",
                    onValueChange = { },
                    errorMessage = "Error message",
                )
                SettingTextField(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .padding(horizontal = 16.dp),
                    placeholder = "Placeholder",
                    value = "Value",
                    onValueChange = { },
                    isSecure = true,
                    errorMessage = "Error message",
                )
            }
        }
    }
}