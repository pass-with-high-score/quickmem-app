package com.pwhs.quickmem.presentation.app.ai_generative.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.LanguageCode

@Composable
fun SelectLanguage(
    modifier: Modifier = Modifier,
    onChooseLanguage: (String) -> Unit,
    onShowBottomSheetLanguage: (Boolean) -> Unit,
    language: String,
) {
    Text(
        text = stringResource(R.string.txt_language),
        style = typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
        ),
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        onClick = {
            onShowBottomSheetLanguage(true)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.txt_language),
                style = typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(
                modifier = Modifier.weight(1f)
            )
            Text(
                text = when (language) {
                    LanguageCode.VI.code -> stringResource(R.string.txt_vietnamese)
                    else -> stringResource(R.string.txt_english_us)
                },
                style = typography.bodyMedium.copy(
                    color = colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.clickable {
                    onChooseLanguage(language)
                }
            )
        }
    }
}