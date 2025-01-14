package com.pwhs.quickmem.presentation.app.study_set.studies.quiz.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pwhs.quickmem.core.data.states.RandomAnswer
import com.pwhs.quickmem.presentation.components.ShowImageDialog

@Composable
fun LearnQuizCardAnswer(
    modifier: Modifier = Modifier,
    correctColor: Color = MaterialTheme.colorScheme.primary,
    incorrectColor: Color = MaterialTheme.colorScheme.error,
    randomAnswer: RandomAnswer,
    selectedAnswer: String,
    onAnswerSelected: (String) -> Unit = {},
) {
    val isSelected = selectedAnswer.isNotEmpty()
    val isCorrect = isSelected && randomAnswer.isCorrect
    val isIncorrect = isSelected && selectedAnswer == randomAnswer.answer
    var isImageViewerOpen by remember { mutableStateOf(false) }
    var definitionImageUri by remember { mutableStateOf("") }

    Card(
        modifier = modifier.fillMaxWidth(),
        enabled = !isSelected,
        border = BorderStroke(
            width = when {
                isCorrect -> 2.dp
                isIncorrect -> 2.dp
                else -> 0.5.dp
            },
            color = when {
                isCorrect -> correctColor
                isIncorrect -> incorrectColor
                else -> MaterialTheme.colorScheme.onSurface
            }
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = {
            onAnswerSelected(randomAnswer.answer)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = when {
                    isCorrect -> Icons.Default.Done
                    isIncorrect -> Icons.Default.Clear
                    else -> Icons.Default.RadioButtonUnchecked
                },
                contentDescription = null,
                tint = when {
                    isCorrect -> correctColor
                    isIncorrect -> incorrectColor
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
            Text(
                text = randomAnswer.answer,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyMedium.copy(),
                color = when {
                    isCorrect -> correctColor
                    isIncorrect -> incorrectColor
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
            if (randomAnswer.answerImage.isNotEmpty()) {
                AsyncImage(
                    model = randomAnswer.answerImage,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        isImageViewerOpen = true
                        definitionImageUri = randomAnswer.answerImage
                    }
                )
            }
        }
    }

    // Image Viewer Dialog
    if (isImageViewerOpen) {
        ShowImageDialog(
            definitionImageUri = definitionImageUri,
            onDismissRequest = {
                isImageViewerOpen = false
                definitionImageUri = ""
            }
        )
    }
}
