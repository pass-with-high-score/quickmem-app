package com.pwhs.quickmem.presentation.auth.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.TextFieldType
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.utils.upperCaseFirstLetter

@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    label: String = "",
    @DrawableRes iconId: Int? = null,
    contentDescription: String = label.upperCaseFirstLetter(),
    readOnly: Boolean = false,
    enabled: Boolean = true,
    type: TextFieldType = TextFieldType.TEXT,
    onClick: (() -> Unit)? = null,
    @StringRes error: Int? = null,
    imeAction: ImeAction = ImeAction.Next,
    onDone: () -> Unit = {},
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Column {
        TextField(
            value = value,
            isError = error != null,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = label,
                    style = typography.bodyLarge.copy(
                        color = colorScheme.onSurface.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            readOnly = readOnly,
            enabled = enabled,
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction,
                keyboardType = when (type) {
                    TextFieldType.EMAIL -> KeyboardType.Email
                    TextFieldType.PASSWORD -> KeyboardType.Password
                    else -> KeyboardType.Text
                }
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onDone()
                }
            ),
            leadingIcon = {
                iconId?.let {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(shapes.extraLarge)
                            .shadow(
                                elevation = 2.dp,
                                shape = shapes.extraLarge
                            )
                            .background(colorScheme.surface, shapes.extraLarge)
                    ) {
                        Image(
                            painter = painterResource(id = iconId),
                            contentDescription = contentDescription,
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(colorScheme.onSurface),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            },
            trailingIcon = {
                if (type == TextFieldType.PASSWORD) {
                    Icon(
                        painter = painterResource(id = if (showPassword) R.drawable.ic_eye else R.drawable.ic_eye_off),
                        contentDescription = "Show password",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                showPassword = !showPassword
                            },
                        tint = colorScheme.onSurface,
                    )
                }
            },
            visualTransformation = if (type == TextFieldType.PASSWORD && !showPassword) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            shape = shapes.medium,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledTextColor = colorScheme.onSurface,
                disabledPlaceholderColor = colorScheme.onSurface,
                focusedTextColor = colorScheme.onSurface,
                focusedPlaceholderColor = colorScheme.onSurface,
                cursorColor = colorScheme.onSurface,
                errorContainerColor = Color.Transparent,
                disabledIndicatorColor = if (error != null) colorScheme.error else colorScheme.onSurface,

            ),

            modifier = modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .then(
                    if (onClick != null) Modifier.clickable { onClick() } else Modifier
                ),
            textStyle = typography.bodyLarge.copy(
                color = colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        )

        error?.let {
            Text(
                text = stringResource(error),
                style = typography.bodySmall.copy(
                    color = colorScheme.error.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthTextFieldPreview() {
    QuickMemTheme {
        AuthTextField(
            value = "",
            onValueChange = {},
            label = "Email",
            iconId = R.drawable.ic_email,
            contentDescription = "Email",
            type = TextFieldType.EMAIL
        )
    }
}

