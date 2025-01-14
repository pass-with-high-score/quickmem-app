package com.pwhs.quickmem.presentation.app.explore.create_study_set_ai.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R

@Composable
fun FABAI(
    modifier: Modifier = Modifier,
    onCreateStudySet: () -> Unit,
    isPlus: Boolean,
    numberOfFlashcards: Int,
) {
    FloatingActionButton(
        onClick = onCreateStudySet,
        shape = MaterialTheme.shapes.large,
        containerColor = colorScheme.primary,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(5.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_sparkling),
                contentDescription = "Create",
            )
            Text(
                text = when (isPlus) {
                    false -> when (numberOfFlashcards) {
                        in 1..10 -> stringResource(
                            R.string.txt_create_study_set_ai_now,
                            1
                        )

                        in 11..20 -> stringResource(
                            R.string.txt_create_study_set_ai_now,
                            2
                        )

                        else -> {
                            stringResource(R.string.txt_create_study_set_ai_now, 3)
                        }
                    }

                    else -> stringResource(
                        R.string.txt_create_study_set_with_ai,
                    )
                },
                style = typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                ),
                textAlign = TextAlign.Center,
            )
        }
    }
}