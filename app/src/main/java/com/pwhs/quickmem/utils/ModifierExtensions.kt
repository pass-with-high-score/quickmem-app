package com.pwhs.quickmem.utils

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
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