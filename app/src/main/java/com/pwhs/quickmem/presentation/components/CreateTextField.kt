package com.pwhs.quickmem.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun CreateTextField(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    valueError: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Text(
            text = title,
            style = typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        OutlinedTextField(
            shape = RoundedCornerShape(10.dp),
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            isError = !valueError.isNullOrEmpty(),
            supportingText = {
                valueError?.let {
                    Text(
                        text = it,
                        style = typography.bodySmall
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            colors = colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = colorScheme.primary,
                focusedSupportingTextColor = colorScheme.error,
                unfocusedSupportingTextColor = colorScheme.error,
                errorContainerColor = Color.Transparent,
                unfocusedIndicatorColor = colorScheme.onSurface.copy(alpha = 0.12f),
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateTextFieldPreview() {
    QuickMemTheme {
        CreateTextField(
            title = "Title",
            value = "Value",
            placeholder = "Placeholder",
            onValueChange = {},
            valueError = ""
        )
    }
}