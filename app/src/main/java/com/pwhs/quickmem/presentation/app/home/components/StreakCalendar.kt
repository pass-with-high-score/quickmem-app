package com.pwhs.quickmem.presentation.app.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.ui.theme.streakTitleColor
import com.pwhs.quickmem.utils.dashedBorder
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreakCalendar(
    modifier: Modifier = Modifier,
    currentDate: LocalDate = LocalDate.now(),
    streakDates: List<LocalDate>,
) {
    val startOfWeek = currentDate.minusDays(currentDate.dayOfWeek.value.toLong() - 1)
    val weekDays = (0..6).map { startOfWeek.plusDays(it.toLong()) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        weekDays.forEach { date ->
            val isToday = date == currentDate
            val hasStreak = streakDates.contains(date)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = date.dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.ENGLISH
                    ),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .dashedBorder(
                            width = if (isToday && !hasStreak) 2.dp else 0.dp,
                            color = if (isToday) Color.Red else Color.Blue,
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (hasStreak) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_fire_date),
                            contentDescription = stringResource(R.string.txt_streak_fire),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = if (hasStreak) Color.White else Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                if (isToday) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .background(streakTitleColor, shape = CircleShape)
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun StreakCalendarPreview() {
    QuickMemTheme {
        StreakCalendar(
            currentDate = LocalDate.now(),
            streakDates = listOf(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(3)
            )
        )
    }
}