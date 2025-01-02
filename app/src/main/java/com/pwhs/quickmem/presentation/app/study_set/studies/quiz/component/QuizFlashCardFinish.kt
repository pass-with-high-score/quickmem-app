package com.pwhs.quickmem.presentation.app.study_set.studies.quiz.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.states.WrongAnswer
import com.pwhs.quickmem.presentation.app.study_set.detail.progress.StudySetDonutChart
import com.pwhs.quickmem.presentation.component.ShowImageDialog
import com.pwhs.quickmem.ui.theme.correctColor
import com.pwhs.quickmem.ui.theme.incorrectColor
import com.pwhs.quickmem.utils.toStringTime

@Composable
fun QuizFlashCardFinish(
    modifier: Modifier = Modifier,
    isEndOfList: Boolean,
    wrongAnswerCount: Int,
    correctAnswerCount: Int,
    studySetColor: Color,
    flashCardSize: Int,
    learningTime: Long,
    onContinueLearningClicked: () -> Unit,
    listWrongAnswer: List<WrongAnswer>,
    onRestartClicked: () -> Unit,
    isGetAll: Boolean
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_firework))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isEndOfList,
    )
    val encouragementMessage = when {
        correctAnswerCount == 0 -> stringResource(R.string.txt_don_t_give_up_keep_trying)
        correctAnswerCount <= flashCardSize * 0.3 -> stringResource(R.string.txt_good_start_keep_going)
        correctAnswerCount <= flashCardSize * 0.6 -> stringResource(R.string.txt_you_re_doing_great_keep_it_up)
        correctAnswerCount < flashCardSize -> stringResource(R.string.txt_almost_there_keep_pushing)
        else -> stringResource(R.string.txt_excellent_you_ve_mastered_it)
    }
    var isImageViewerOpen by remember { mutableStateOf(false) }
    var definitionImageUri by remember { mutableStateOf("") }
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f)
        )
        LazyColumn {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = encouragementMessage,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .padding(16.dp)
                            .padding(top = 32.dp)
                    )
                    StudySetDonutChart(
                        modifier = Modifier
                            .size(200.dp),
                        studySetsStillLearn = wrongAnswerCount.coerceAtLeast(0),
                        studySetsMastered = correctAnswerCount.coerceAtLeast(0),
                        color = studySetColor
                    )

                    Text(
                        text = stringResource(R.string.txt_keep_focusing_on_your_study_set_to_master_it),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(16.dp)
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        border = BorderStroke(1.dp, studySetColor),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp,
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_card),
                                contentDescription = "Card",
                            )
                            Text(
                                text = stringResource(R.string.txt_flashcards_learned),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 18.sp
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(
                                        start = 8.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        append(correctAnswerCount.coerceAtLeast(0).toString())
                                    }
                                    append("/${flashCardSize}")
                                },
                                fontSize = 18.sp,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        border = BorderStroke(1.dp, studySetColor),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp,
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "Time",
                            )
                            Text(
                                text = stringResource(R.string.txt_learning_time),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 18.sp
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(
                                        start = 8.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = learningTime.toStringTime(),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                    if (wrongAnswerCount > 0) {
                        Button(
                            onClick = {
                                onContinueLearningClicked()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .padding(horizontal = 16.dp),
                            shape = MaterialTheme.shapes.small,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = studySetColor,
                                contentColor = Color.White
                            ),
                            border = BorderStroke(1.dp, studySetColor)
                        ) {
                            Text(
                                text = when (isGetAll) {
                                    true -> {
                                        stringResource(
                                            R.string.txt_finish_answers_now,
                                            wrongAnswerCount
                                        )
                                    }

                                    false -> {
                                        stringResource(
                                            R.string.txt_continue_learning,
                                            wrongAnswerCount
                                        )
                                    }
                                },
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                    if (isGetAll) {
                        Button(
                            onClick = {
                                onRestartClicked()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .padding(horizontal = 16.dp),
                            shape = MaterialTheme.shapes.small,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = studySetColor
                            ),
                            border = BorderStroke(1.dp, studySetColor)
                        ) {
                            Text(
                                text = stringResource(R.string.txt_learn_from_the_beginning),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.txt_you_have_finished_this_section_of_study_set),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 18.sp,
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
                }
            }
            if (wrongAnswerCount < 0) {
                item {
                    Text(
                        text = stringResource(R.string.txt_review_your_wrong_answers),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    )
                }
            }
            items(listWrongAnswer) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.txt_term_flashcard,
                                    it.flashCard.term
                                ),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            if (!it.flashCard.definitionImageURL.isNullOrEmpty()) {
                                AsyncImage(
                                    model = it.flashCard.definitionImageURL,
                                    contentDescription = "Definition Image",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable {
                                            isImageViewerOpen = true
                                            definitionImageUri = it.flashCard.definitionImageURL
                                        },
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Text(
                            text = stringResource(
                                R.string.txt_correct_answer,
                                it.flashCard.definition
                            ),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = correctColor
                            )
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            text = stringResource(R.string.txt_your_answer, it.userAnswer),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = incorrectColor
                            )
                        )
                    }
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
}