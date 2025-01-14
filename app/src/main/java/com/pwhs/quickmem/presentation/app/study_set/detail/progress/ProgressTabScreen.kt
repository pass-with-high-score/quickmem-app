package com.pwhs.quickmem.presentation.app.study_set.detail.progress

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.data.mapper.study_time.toStudyTimeModel
import com.pwhs.quickmem.domain.model.study_time.GetStudyTimeByStudySetResponseModel
import com.pwhs.quickmem.presentation.components.LearningTimeBars
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun ProgressTabScreen(
    modifier: Modifier = Modifier,
    totalStudySet: Int = 0,
    studySetsNotLearnCount: Int = 0,
    studySetsStillLearningCount: Int = 0,
    studySetsKnowCount: Int = 0,
    studyTime: GetStudyTimeByStudySetResponseModel? = null,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                Text(
                    text = stringResource(R.string.txt_your_progress),
                    style = typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when {
                        totalStudySet == 0 -> {
                            Text(
                                text = stringResource(R.string.txt_you_have_not_started_any_study_set_yet),
                                style = typography.bodyMedium.copy(
                                    fontSize = 16.sp
                                )
                            )
                        }

                        else -> {
                            StudySetDonutChart(
                                color = color,
                                studySetsNotLearn = studySetsNotLearnCount,
                                studySetsStillLearn = studySetsStillLearningCount,
                                studySetsMastered = studySetsKnowCount
                            )
                            ProgressRow(
                                label = stringResource(R.string.txt_not_learned),
                                percentage = studySetsNotLearnCount * 100 / totalStudySet,
                                color = color.copy(alpha = 0.3f)
                            )

                            ProgressRow(
                                label = stringResource(R.string.txt_still_learning),
                                percentage = studySetsStillLearningCount * 100 / totalStudySet,
                                color = color.copy(alpha = 0.6f)
                            )

                            ProgressRow(
                                label = stringResource(R.string.txt_learn),
                                percentage = studySetsKnowCount * 100 / totalStudySet,
                                color = color
                            )
                            Text(
                                text = stringResource(R.string.txt_current_flip_status),
                                style = typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                )
                            )
                        }
                    }
                }
            }
            item {
                AnimatedVisibility(
                    visible = studyTime?.flip != 0 || studyTime.quiz != 0 || studyTime.total != 0 || studyTime.write != 0,
                ) {
                    LearningTimeBars(
                        studyTime = studyTime?.toStudyTimeModel(),
                        color = color,
                        modifier = Modifier
                            .height(310.dp)
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.padding(60.dp))
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun PressTabScreen() {
    QuickMemTheme {
        Scaffold { innerPadding ->
            ProgressTabScreen(
                modifier = Modifier.padding(innerPadding),
                totalStudySet = 10,
                studySetsNotLearnCount = 3,
                studySetsStillLearningCount = 4,
                studySetsKnowCount = 3
            )
        }
    }
}