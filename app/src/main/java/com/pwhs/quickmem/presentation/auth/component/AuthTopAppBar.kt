package com.pwhs.quickmem.presentation.auth.component

import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults.iconButtonColors
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pwhs.quickmem.R
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTopAppBar(
    onClick: () -> Unit = {},
) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = Color.Transparent,
        ),
        navigationIcon = {
            IconButton(
                onClick = onClick,
                colors = iconButtonColors(
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Filled.ArrowBack,
                    contentDescription = stringResource(R.string.txt_back),
                )
            }
        },
        title = {
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun AuthTopAppBarPreview() {
    QuickMemTheme {
        AuthTopAppBar()
    }
}