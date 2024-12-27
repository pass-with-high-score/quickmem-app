package com.pwhs.quickmem.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemsTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onNavigateCancel: () -> Unit,
    onDoneClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        title = {
            Text(
                text = title,
                style = typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface,
                    fontSize = 20.sp
                )
            )
        },
        actions = {
            TextButton(
                onClick = onDoneClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colorScheme.onSurface
                )
            ) {
                Text(
                    text = stringResource(R.string.txt_done),
                    style = typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                )
            }
        },
        navigationIcon = {
            TextButton(
                onClick = onNavigateCancel,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colorScheme.onSurface
                )
            ) {
                Text(
                    text = stringResource(R.string.txt_cancel),
                    style = typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun AddStudySetToClassesTopAppBarPreview() {
    QuickMemTheme {
        Scaffold(
            topBar = {
                AddItemsTopAppBar(
                    title = "Add to folder",
                    onNavigateCancel = {},
                    onDoneClick = {}
                )
            }
        ) {
            Column(
                modifier = Modifier.padding(it)
            ) { }
        }
    }
}