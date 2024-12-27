package com.pwhs.quickmem.presentation.app.search_result.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.IconButtonDefaults.iconButtonColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
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
fun TopBarSearch(
    modifier: Modifier = Modifier,
    title: String,
    onNavigateBack: () -> Unit,
    onResetClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = topAppBarColors(
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
                onClick = onResetClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colorScheme.onSurface
                )
            ) {
                Text(
                    text = stringResource(R.string.txt_reset),
                    style = typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigateBack,
                colors = iconButtonColors(
                    contentColor = colorScheme.onSurface
                )
            ) {
                Icon(
                    imageVector = AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.txt_back),
                )
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun TopBarSearchPreview() {
    QuickMemTheme {
        Scaffold(
            topBar = {
                TopBarSearch(
                    title = "Filters",
                    onNavigateBack = {},
                    onResetClick = {}
                )
            }
        ) {
            Column (
                modifier = Modifier.padding(it)
            ){  }
        }
    }
}