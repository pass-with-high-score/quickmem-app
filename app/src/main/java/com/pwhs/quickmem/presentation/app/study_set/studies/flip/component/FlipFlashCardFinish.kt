package com.pwhs.quickmem.presentation.app.study_set.studies.flip.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.pwhs.quickmem.R
import com.pwhs.quickmem.presentation.app.study_set.detail.progress.StudySetDonutChart
import com.pwhs.quickmem.utils.toStringTime

@Composable
fun FlipFlashCardFinish(
    modifier: Modifier = Modifier,
    isEndOfList: Boolean,
    countStillLearning: Int,
    countKnown: Int,
    studySetColor: Color,
    flashCardSize: Int,
    learningTime: Long,
    onContinueLearningClicked: () -> Unit,
    onRestartClicked: () -> Unit,
    isGetAll: Boolean
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_firework))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isEndOfList,
    )
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
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.txt_you_re_doing_great_keep_it_up),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(top = 32.dp)
                )
            }
            item {
                StudySetDonutChart(
                    modifier = Modifier
                        .size(200.dp),
                    studySetsStillLearn = countStillLearning.coerceAtLeast(0),
                    studySetsMastered = countKnown.coerceAtLeast(0),
                    color = studySetColor
                )
            }

            item {
                Text(
                    text = stringResource(R.string.txt_keep_focusing_on_your_study_set_to_master_it),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }

            item {
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
                        Image(
                            painter = painterResource(id = R.drawable.ic_study_set),
                            contentDescription = "Card",
                            contentScale = ContentScale.Crop
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
                                    append(countKnown.coerceAtLeast(0).toString())
                                }
                                append("/${flashCardSize}")
                            },
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
            item {
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
                        Image(
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

            }
            item {
                if (countStillLearning > 0) {
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
                                true -> stringResource(
                                    R.string.txt_keep_reviewing_terms_flip,
                                    countStillLearning
                                )

                                false -> stringResource(
                                    R.string.txt_keep_learning_terms_flip,
                                    countStillLearning
                                )
                            },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
            item {
                if (isEndOfList) {
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
                            text = stringResource(R.string.txt_restart_flashcards),
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
    }
}