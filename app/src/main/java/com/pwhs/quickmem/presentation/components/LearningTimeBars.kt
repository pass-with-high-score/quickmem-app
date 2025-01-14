package com.pwhs.quickmem.presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.study_time.StudyTimeModel
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorPosition
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.LineProperties

@Composable
fun LearningTimeBars(
    modifier: Modifier = Modifier,
    studyTime: StudyTimeModel? = null,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ColumnChart(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            data = listOf(
                Bars(
                    label = stringResource(R.string.txt_flip_card),
                    values = listOf(
                        Bars.Data(
                            value = (studyTime?.flip?.toDouble() ?: 0.0) / 1000,
                            color = Brush.verticalGradient(
                                listOf(
                                    color.copy(alpha = 0.3f),
                                    color
                                )
                            )
                        ),
                    )
                ),
                Bars(
                    label = stringResource(R.string.txt_quiz),
                    values = listOf(
                        Bars.Data(
                            value = (studyTime?.quiz?.toDouble() ?: 0.0) / 1000,
                            color = Brush.verticalGradient(
                                listOf(
                                    color.copy(alpha = 0.3f),
                                    color
                                )
                            )
                        ),
                    )
                ),
                Bars(
                    label = stringResource(R.string.txt_true_false),
                    values = listOf(
                        Bars.Data(
                            value = (studyTime?.trueFalse?.toDouble() ?: 0.0) / 1000,
                            color = Brush.verticalGradient(
                                listOf(
                                    color.copy(alpha = 0.3f),
                                    color
                                )
                            )
                        ),
                    )
                ),
                Bars(
                    label = stringResource(R.string.txt_write),
                    values = listOf(
                        Bars.Data(
                            value = (studyTime?.write?.toDouble() ?: 0.0) / 1000,
                            color = Brush.verticalGradient(
                                listOf(
                                    color.copy(alpha = 0.3f),
                                    color
                                )
                            )
                        ),
                    )
                ),
            ),
            indicatorProperties = HorizontalIndicatorProperties(
                enabled = true,
                position = IndicatorPosition.Horizontal.Start,
                contentBuilder = { value ->
                    when {
                        value < 100 -> "${value.toInt()}s"
                        value < 3600 -> "${(value / 60).toInt()}m"
                        else -> "${(value / 3600).toInt()}h"
                    }
                }
            ),
            dividerProperties = DividerProperties(
                enabled = true,
                yAxisProperties = LineProperties(
                    color = Brush.verticalGradient(
                        listOf(
                            color.copy(alpha = 0.3f),
                            color
                        )
                    ),
                    thickness = 1.dp
                ),
                xAxisProperties = LineProperties(
                    color = Brush.verticalGradient(
                        listOf(
                            color.copy(alpha = 0.3f),
                            color
                        )
                    ),
                    thickness = 1.dp
                )
            ),
            labelHelperProperties = LabelHelperProperties(
                enabled = false
            ),
            labelProperties = LabelProperties(
                textStyle = typography.bodyMedium.copy(
                    fontSize = 16.sp,
                ),
                enabled = true
            ),
            minValue = 0.0,
            maxValue = studyTime?.let {
                (listOfNotNull(
                    it.flip,
                    it.quiz,
                    it.trueFalse,
                    it.write
                ).maxOrNull() ?: 0) / 1000 + (2..5).random()
            }?.toDouble() ?: 0.0,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
        )
        Spacer(modifier = Modifier.padding(vertical = 30.dp))
        Text(
            text = stringResource(R.string.txt_time_spent_on_each_learning_type),
            style = typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}