package com.pwhs.quickmem.presentation.app.search_result.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults.iconButtonColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarSearchResult(
    modifier: Modifier = Modifier,
    title: String,
    onNavigateBack: () -> Unit,
    onClickFilter: () -> Unit = {},
    showFilterIcon: Boolean = false,
) {
    TopAppBar(
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
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            if (showFilterIcon) {
                IconButton(
                    onClick = onClickFilter,
                    colors = iconButtonColors(
                        contentColor = colorScheme.onSurface
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = stringResource(R.string.txt_filter),
                    )
                }
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
fun CreateTopAppBarPreview() {
    QuickMemTheme {
        Scaffold(
            topBar = {
                TopBarSearchResult(
                    title = "Result",
                    onNavigateBack = {},
                )
            }
        ) {
            Column(
                modifier = Modifier.padding(it)
            ) { }
        }
    }
}