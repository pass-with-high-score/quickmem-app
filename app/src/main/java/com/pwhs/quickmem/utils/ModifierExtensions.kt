package com.pwhs.quickmem.utils

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.ui.theme.primaryLight

fun Modifier.gradientBackground(): Modifier {
    return this.background(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF92DFF3),
                Color(0xFFB7E9F7),
                Color(0xFFDBF3FA),
                Color(0xFFF5FCFF),
            ),
            tileMode = TileMode.Mirror
        )
    )
}

fun Modifier.splashBackground(): Modifier {
    return this.background(
        brush = Brush.verticalGradient(
            colors = listOf(
                primaryLight,
                primaryLight
            ),
            tileMode = TileMode.Mirror
        )
    )
}

fun Modifier.dashedBorder(
    width: Dp,
    color: Color,
    dashWidth: Dp = 4.dp,
    dashGap: Dp = 4.dp
): Modifier = this.drawBehind {
    val strokeWidth = width.value
    val dashWidthPx = dashWidth.value
    val dashGapPx = dashGap.value

    drawCircle(
        color = color,
        radius = size.minDimension / 2,
        style = Stroke(width = strokeWidth, pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashWidthPx, dashGapPx), 0f))
    )
}