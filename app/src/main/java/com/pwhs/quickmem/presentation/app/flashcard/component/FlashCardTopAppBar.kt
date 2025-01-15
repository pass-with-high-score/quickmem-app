package com.pwhs.quickmem.presentation.app.flashcard.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.pwhs.quickmem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashCardTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onNavigationBack: () -> Unit,
    onSaveFlashCardClicked: () -> Unit,
    enableSaveButton: Boolean,
    onSettingsClicked: () -> Unit,
    color: Color,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationBack) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = stringResource(R.string.txt_back)
                )
            }
        },
        actions = {
            IconButton(onClick = onSettingsClicked) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.txt_settings)
                )
            }
            IconButton(
                onClick = onSaveFlashCardClicked,
                enabled = enableSaveButton
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(R.string.txt_save),
                    tint = if (enableSaveButton) {
                        color
                    } else {
                        colorScheme.onSurface.copy(alpha = 0.5f)
                    }
                )
            }
        }
    )
}