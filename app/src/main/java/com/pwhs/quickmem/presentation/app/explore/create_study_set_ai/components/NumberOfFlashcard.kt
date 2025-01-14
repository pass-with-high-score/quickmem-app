package com.pwhs.quickmem.presentation.app.explore.create_study_set_ai.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R

@Composable
fun NumberOfFlashcard(
    modifier: Modifier = Modifier,
    numberOfFlashcards: Int,
    onNumberOfFlashcardsChange: (Int) -> Unit,
) {
    Text(
        text = stringResource(R.string.txt_number_of_flashcards_optional),
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("5")
                RadioButton(
                    selected = numberOfFlashcards == 5,
                    onClick = { onNumberOfFlashcardsChange(5) }
                )
            }
            VerticalDivider(
                modifier = Modifier
                    .height(30.dp)
                    .padding(horizontal = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("10")
                RadioButton(
                    selected = numberOfFlashcards == 10,
                    onClick = { onNumberOfFlashcardsChange(10) }
                )
            }
            VerticalDivider(
                modifier = Modifier
                    .height(30.dp)
                    .padding(horizontal = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("15")
                RadioButton(
                    selected = numberOfFlashcards == 15,
                    onClick = { onNumberOfFlashcardsChange(15) }
                )
            }
            VerticalDivider(
                modifier = Modifier
                    .height(30.dp)
                    .padding(horizontal = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("20")
                RadioButton(
                    selected = numberOfFlashcards == 20,
                    onClick = { onNumberOfFlashcardsChange(20) }
                )
            }
        }
    }
}