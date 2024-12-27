package com.pwhs.quickmem.presentation.app.profile.change_avatar.component

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
import com.pwhs.quickmem.R
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeAvatarTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onDoneClick: () -> Unit,
    onNavigateBack:() ->Unit
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
                    )
                )
            }
        },
        navigationIcon = {
            TextButton(
                onClick = onNavigateBack,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colorScheme.onSurface
                )
            ) {
                Text(
                    text = stringResource(R.string.txt_cancel),
                    style = typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface,
                    )
                )
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun ChangeAvatarTopAppBarTopAppBarPreview() {
    QuickMemTheme {
        Scaffold(
            topBar = {
                ChangeAvatarTopAppBar(
                    title = stringResource(R.string.txt_choose_a_picture),
                    onDoneClick = {},
                    onNavigateBack = {}
                )
            }
        ) {
            Column (
                modifier = Modifier.padding(it)
            ){  }
        }
    }
}