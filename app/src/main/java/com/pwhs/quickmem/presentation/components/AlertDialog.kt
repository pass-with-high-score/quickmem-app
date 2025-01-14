package com.pwhs.quickmem.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun QuickMemAlertDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    text: String,
    confirmButtonTitle: String,
    dismissButtonTitle: String,
    buttonColor: Color = colorScheme.primary,
) {
    AlertDialog(
        containerColor = Color.White,
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = title,
                style = typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground
                )
            )
        },
        text = {
            Text(
                text = text,
                style = typography.bodyMedium,
                color = colorScheme.onBackground
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor
                )
            ) {
                Text(confirmButtonTitle, color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismissRequest
            ) {
                Text(dismissButtonTitle, color = colorScheme.onBackground)
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun QuickMemAlertDialogPreview() {
    QuickMemAlertDialog(
        onDismissRequest = {},
        onConfirm = {},
        title = "Delete Flashcard",
        text = "Are you sure you want to delete this flashcard?",
        confirmButtonTitle = "Delete",
        dismissButtonTitle = "Cancel",
        buttonColor = colorScheme.error
    )
}