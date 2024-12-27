package com.pwhs.quickmem.presentation.app.search_result.all_result.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R

@Composable
fun SectionHeader(
    title: String,
    onSeeAllClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = typography.titleLarge,
            textAlign = TextAlign.Center
        )
        TextButton(
            onClick = onSeeAllClick
        ) {
            Text(
                text = stringResource(R.string.txt_view_all),
                style = typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}