package com.pwhs.quickmem.presentation.app.settings.about.open_source_licenses

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.presentation.app.settings.about.open_source_licenses.data.LicensesModel
import com.pwhs.quickmem.presentation.app.settings.about.open_source_licenses.data.sourceLicensesList
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun OpenSourceScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: OpenSourceViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is OpenSourceUiEvent.NavigateToDetail -> {
                    val intent: Intent = Intent(Intent.ACTION_VIEW).apply {
                        data = event.license.linkSource.toUri()
                    }
                    context.startActivity(intent)
                }

                is OpenSourceUiEvent.ShowError -> {
                    event.message
                }
            }
        }
    }

    OpenSourceLicenses(
        modifier = modifier,
        licenses = state.licenses,
        onClickSource = { licenseId ->
            viewModel.onEvent(OpenSourceUiAction.LicenseClicked(licenseId))
        },
        onNavigateBack = {
            navigator.navigateUp()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenSourceLicenses(
    modifier: Modifier = Modifier,
    licenses: List<LicensesModel> = emptyList(),
    onClickSource: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = modifier,
                title = {
                    Text(
                        text = stringResource(R.string.txt_opensource_license),
                        style = typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface
                        ),
                    )
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
                },
                colors = topAppBarColors(
                    containerColor = Color.Transparent,
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            items(licenses) { license ->
                Card(
                    modifier = modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(0.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent,
                    ),
                    onClick = {
                        onClickSource(license.id)
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = license.title,
                            style = typography.titleMedium
                        )
                    }
                }
                HorizontalDivider(
                    color = colorScheme.onSurface.copy(alpha = 0.12f)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun OpensourceScreenPreview() {
    QuickMemTheme {
        OpenSourceLicenses(
            licenses = sourceLicensesList,
        )
    }
}

