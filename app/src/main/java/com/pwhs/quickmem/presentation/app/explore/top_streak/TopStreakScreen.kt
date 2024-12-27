package com.pwhs.quickmem.presentation.app.explore.top_streak

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.streak.GetTopStreakResponseModel
import com.pwhs.quickmem.presentation.app.explore.top_streak.component.StreakItem
import com.pwhs.quickmem.ui.theme.premiumColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopStreakScreen(
    isLoading: Boolean = false,
    rankOwner: Int? = null,
    streakOwner: GetTopStreakResponseModel? = null,
    topStreaks: List<GetTopStreakResponseModel> = emptyList(),
    onClickToUserDetail: (GetTopStreakResponseModel) -> Unit = {},
    onTopStreakRefresh: () -> Unit = {},
) {
    val refreshState = rememberPullToRefreshState()
    val silverSize = remember { Animatable(128f) }
    val goldSize = remember { Animatable(128f) }
    val bronzeSize = remember { Animatable(128f) }

    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        bronzeSize.animateTo(256f, animationSpec = tween(durationMillis = 800))
        delay(200)
        silverSize.animateTo(256f, animationSpec = tween(durationMillis = 800))
        delay(200)
        goldSize.animateTo(320f, animationSpec = tween(durationMillis = 800))
    }

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        state = refreshState,
        isRefreshing = isLoading,
        onRefresh = {
            onTopStreakRefresh()
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(124.dp)
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.top2),
                        contentDescription = stringResource(R.string.txt_silver_medal),
                        modifier = Modifier.size(with(density) { silverSize.value.toDp() })
                    )
                    Image(
                        painter = painterResource(id = R.drawable.top1),
                        contentDescription = stringResource(R.string.txt_gold_medal),
                        modifier = Modifier.size(with(density) { goldSize.value.toDp() })
                    )
                    Image(
                        painter = painterResource(id = R.drawable.top3),
                        contentDescription = stringResource(R.string.txt_bronze_medal),
                        modifier = Modifier.size(with(density) { bronzeSize.value.toDp() })
                    )
                }
            }
            item {
                val message = when (rankOwner) {
                    in 1..3 -> stringResource(
                        R.string.txt_you_have_reached_the_top_on_the_top_streak_leaderboard,
                        rankOwner ?: 0
                    )

                    in 4..10 -> stringResource(R.string.txt_streak_leaderboard_motivation)
                    else -> stringResource(R.string.txt_top_10_highest_streak_leaderboard)
                }
                Text(
                    text = message,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = premiumColor,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .padding(horizontal = 16.dp),
                )
                HorizontalDivider(
                    color = Color.Gray.copy(alpha = 0.1f),
                    thickness = 1.dp,
                )
            }
            items(topStreaks.size) { index ->
                val topStreak = topStreaks[index]
                StreakItem(
                    rank = index + 1,
                    topStreak = topStreak,
                    onClickToUserDetail = onClickToUserDetail,
                    isCurrentUser = topStreak.userId == streakOwner?.userId
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun TopStreakScreenPreview() {
    MaterialTheme {
        TopStreakScreen()
    }
}