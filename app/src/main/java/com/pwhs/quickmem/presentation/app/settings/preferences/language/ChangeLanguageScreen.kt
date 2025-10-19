package com.pwhs.quickmem.presentation.app.settings.preferences.language

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.LanguageCode
import com.pwhs.quickmem.core.utils.AppConstant.FOLLOW_SYSTEM
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.utils.changeLanguage
import com.pwhs.quickmem.utils.getLanguageCode
import com.pwhs.quickmem.utils.isFollowSystem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination<RootGraph>
@Composable
fun ChangeLanguageScreen(
    modifier: Modifier = Modifier,
    resultNavigator: ResultBackNavigator<Boolean>
) {
    val context = LocalContext.current
    ChangeLanguage(
        modifier = modifier,
        languageCode = if (context.isFollowSystem()) FOLLOW_SYSTEM else context.getLanguageCode(),
        onLanguageCodeChanged = { code ->
            context.changeLanguage(code)
            resultNavigator.navigateBack(true)
        },
        onNavigateUp = {
            resultNavigator.navigateBack(true)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChangeLanguage(
    modifier: Modifier = Modifier,
    languageCode: String,
    onLanguageCodeChanged: (String) -> Unit,
    onNavigateUp: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.txt_change_language),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateUp
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.txt_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Card(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onLanguageCodeChanged(FOLLOW_SYSTEM)
                    }
                ) {
                    RadioButton(
                        selected = languageCode == FOLLOW_SYSTEM,
                        onClick = { onLanguageCodeChanged(FOLLOW_SYSTEM) }
                    )
                    Text(
                        text = stringResource(R.string.txt_follow_system),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
                HorizontalDivider()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onLanguageCodeChanged(LanguageCode.EN.code)
                    }
                ) {
                    RadioButton(
                        selected = languageCode == LanguageCode.EN.code,
                        onClick = { onLanguageCodeChanged(LanguageCode.EN.code) }
                    )
                    Text(
                        text = stringResource(LanguageCode.EN.displayName),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Image(
                        painter = painterResource(id = LanguageCode.EN.icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)

                    )
                }
                HorizontalDivider()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onLanguageCodeChanged(LanguageCode.VI.code)
                    }
                ) {
                    RadioButton(
                        selected = languageCode == LanguageCode.VI.code,
                        onClick = { onLanguageCodeChanged(LanguageCode.VI.code) }
                    )
                    Text(
                        text = stringResource(LanguageCode.VI.displayName),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Image(
                        painter = painterResource(id = LanguageCode.VI.icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ChangeLanguageScreenPreview() {
    QuickMemTheme {
        ChangeLanguage(
            languageCode = LanguageCode.EN.code,
            onLanguageCodeChanged = {}
        )
    }
}
