package com.pwhs.quickmem.presentation.auth.welcome.component

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScrollingText(textList: List<String>, displayCount: Int, currentIndex: Int) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(vertical = 20.dp)
    ) {
        for (i in 0 until displayCount) {
            val index = (currentIndex + i) % textList.size
            val alphaValue = when (i) {
                2 -> 1f
                1, 3 -> 0.6f
                0 -> 0.3f
                else -> 1f
            }
            val alpha by animateFloatAsState(
                targetValue = alphaValue,
                animationSpec = tween(
                    durationMillis = 2000,
                    easing = LinearOutSlowInEasing
                ),
                label = "alpha"
            )
            Text(
                text = textList[index],
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = alpha),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
