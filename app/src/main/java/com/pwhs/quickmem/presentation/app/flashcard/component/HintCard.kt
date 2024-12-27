package com.pwhs.quickmem.presentation.app.flashcard.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R

@Composable
fun HintCard(
    modifier: Modifier = Modifier,
    hint: String,
    onHintChanged: (String) -> Unit,
    onShowHintClicked: (Boolean) -> Unit,
    color: Color,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp,
            focusedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        ),
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                FlashCardTextField(
                    value = hint,
                    onValueChange = onHintChanged,
                    hint = stringResource(R.string.txt_hint),
                    color = color
                )
            }

            IconButton(
                onClick = {
                    onShowHintClicked(false)
                    onHintChanged("")
                },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = stringResource(R.string.txt_close),
                )
            }
        }
    }
}