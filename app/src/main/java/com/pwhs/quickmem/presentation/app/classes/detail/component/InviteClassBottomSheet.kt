package com.pwhs.quickmem.presentation.app.classes.detail.component

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteClassBottomSheet(
    modifier: Modifier = Modifier,
    username: String,
    errorMessage: String,
    onSubmitClick: () -> Unit = {},
    showInviteClassBottomSheet: Boolean = false,
    onUsernameChanged: (String) -> Unit = {},
    sheetShowMoreState: SheetState = rememberModalBottomSheetState(),
    onDismissRequest: () -> Unit = {},
) {
    if (showInviteClassBottomSheet) {
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            sheetState = sheetShowMoreState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    "Invite Members",
                    style = typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "To invite members to this class, add their Quizlet usernames or emails below (separate by commas or line breaks).",
                    style = typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                ClassTextField(
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                    value = username,
                    onValueChange = onUsernameChanged,
                    errorMessage = errorMessage,
                    placeholder = "Type username to invite",
                )
                Button(
                    enabled = username.isNotEmpty() && username.length > 4,
                    onClick = onSubmitClick,
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = shapes.medium
                ) {
                    Text(
                        "Submit", style = typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
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
                        "Cancel",
                        style = typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}