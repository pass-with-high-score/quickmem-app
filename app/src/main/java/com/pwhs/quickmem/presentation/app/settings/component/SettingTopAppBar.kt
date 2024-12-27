package com.pwhs.quickmem.presentation.app.settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pwhs.quickmem.R
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    onNavigateBack: () -> Unit = { },
    onSaved: () -> Unit = { },
    enabled: Boolean = true,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = typography.titleMedium
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigateBack
            ) {
                Icon(
                    imageVector = AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.txt_back)
                )
            }
        },
        actions = {
            IconButton(
                onClick = onSaved,
                enabled = enabled
            ) {
                Icon(
                    imageVector = Default.Done,
                    contentDescription = stringResource(R.string.txt_save)
                )
            }
        }
    )
}

@Preview
@Composable
private fun SettingTopAppBarPreview() {
    QuickMemTheme {
        Scaffold(
            topBar = {
                SettingTopAppBar(
                    title = "Title",
                )
            }
        ) {
            Column(
                modifier = Modifier.padding(it)
            ) { }
        }
    }
}