package com.pwhs.quickmem.presentation.app.study_set.studies.quiz.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.QuizStatus
import com.pwhs.quickmem.core.data.states.RandomAnswer
import com.pwhs.quickmem.domain.model.flashcard.FlashCardResponseModel
import com.pwhs.quickmem.presentation.components.ShowImageDialog
import com.pwhs.quickmem.ui.theme.correctColor
import com.pwhs.quickmem.ui.theme.incorrectColor

@Composable
fun QuizView(
    modifier: Modifier = Modifier,
    flashCard: FlashCardResponseModel,
    randomAnswer: List<RandomAnswer> = emptyList(),
    onCorrectAnswer: (QuizStatus, String) -> Unit,
    canResetState: Boolean = false
) {
    val feedbackString = stringResource(R.string.txt_select_an_answer)
    var isSelectCorrectAnswer by remember { mutableStateOf(false) }
    var selectedAnswer by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf(feedbackString) }
    val correctMessages = listOf(
        stringResource(R.string.txt_great_job),
        stringResource(R.string.txt_well_done),
        stringResource(R.string.txt_correct),
        stringResource(R.string.txt_nice_work)
    )
    val incorrectMessages = listOf(
        stringResource(R.string.txt_try_again),
        stringResource(R.string.txt_incorrect),
        stringResource(R.string.txt_not_quite),
        stringResource(R.string.txt_keep_trying)
    )
    var isImageViewerOpen by remember { mutableStateOf(false) }
    var questionImageUri by remember { mutableStateOf("") }
    LaunchedEffect(key1 = canResetState) {
        selectedAnswer = ""
        feedbackMessage = feedbackString
        isSelectCorrectAnswer = false
    }

    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        text = flashCard.term,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = when {
                                flashCard.term.length <= 10 -> 25.sp
                                flashCard.term.length <= 20 -> 20.sp
                                flashCard.term.length <= 30 -> 18.sp
                                else -> 16.sp
                            }
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    flashCard.termImageURL?.let { url ->
                        if (url.isEmpty()) {
                            return@let
                        }
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(flashCard.termImageURL)
                                .error(R.drawable.ic_image_error)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(16.dp)
                                .clickable {
                                    isImageViewerOpen = true
                                    questionImageUri = flashCard.termImageURL
                                },
                        )
                    }
                }

                Text(
                    text = feedbackMessage,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = when {
                            isSelectCorrectAnswer -> FontWeight.Bold
                            else -> FontWeight.Normal
                        },
                        color = when {
                            correctMessages.contains(feedbackMessage) -> correctColor
                            incorrectMessages.contains(feedbackMessage) -> incorrectColor
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    ),
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                if (selectedAnswer.isNotEmpty() && flashCard.explanation?.isNotEmpty() == true) {
                    Text(
                        text = stringResource(
                            R.string.txt_flashcard_explanation,
                            flashCard.explanation
                        ),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
            items(randomAnswer) { randomAnswer ->
                LearnQuizCardAnswer(
                    correctColor = correctColor,
                    incorrectColor = incorrectColor,
                    randomAnswer = randomAnswer,
                    selectedAnswer = selectedAnswer,
                    onAnswerSelected = {
                        selectedAnswer = it
                        if (selectedAnswer == randomAnswer.answer && randomAnswer.isCorrect) {
                            feedbackMessage = correctMessages.random()
                            onCorrectAnswer(QuizStatus.CORRECT, selectedAnswer)
                            isSelectCorrectAnswer = true
                        } else {
                            feedbackMessage = incorrectMessages.random()
                            onCorrectAnswer(QuizStatus.WRONG, selectedAnswer)
                            isSelectCorrectAnswer = false
                        }
                    }
                )
            }
        }
    }

    // Image Viewer Dialog
    if (isImageViewerOpen) {
        ShowImageDialog(
            definitionImageUri = questionImageUri,
            onDismissRequest = {
                isImageViewerOpen = false
                questionImageUri = ""
            }
        )
    }
}
