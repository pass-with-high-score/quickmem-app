package com.pwhs.quickmem.presentation.app.ai_generative.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.DifficultyLevel

@Composable
fun SelectDifficultLevel(
    modifier: Modifier = Modifier,
    difficultyLevel: DifficultyLevel,
    onDifficultyLevelChange: (DifficultyLevel) -> Unit,
) {
    Text(
        text = stringResource(R.string.txt_difficulty_level),
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
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = stringResource(R.string.txt_easy))
                Spacer(modifier = Modifier.weight(1f))
                RadioButton(
                    selected = difficultyLevel == DifficultyLevel.EASY,
                    onClick = { onDifficultyLevelChange(DifficultyLevel.EASY) }
                )
            }
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = stringResource(R.string.txt_medium))
                Spacer(modifier = Modifier.weight(1f))
                RadioButton(
                    selected = difficultyLevel == DifficultyLevel.MEDIUM,
                    onClick = { onDifficultyLevelChange(DifficultyLevel.MEDIUM) }
                )
            }
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = stringResource(R.string.txt_hard))
                Spacer(modifier = Modifier.weight(1f))
                RadioButton(
                    selected = difficultyLevel == DifficultyLevel.HARD,
                    onClick = { onDifficultyLevelChange(DifficultyLevel.HARD) }
                )
            }
        }
    }
}