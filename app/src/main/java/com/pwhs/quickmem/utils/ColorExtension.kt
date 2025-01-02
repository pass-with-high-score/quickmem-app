package com.pwhs.quickmem.utils

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red

fun Color.gradientBackground(): Brush {
    return Brush.verticalGradient(
        colors = listOf(
            this.copy(alpha = 0.8f),
            this.copy(alpha = 0.6f),
            this.copy(alpha = 0.4f),
            this.copy(alpha = 0.2f),
            this.copy(alpha = 0.0f)
        )
    )
}
fun Color.convertToOldColor(): Int {
    val color = this.toArgb()
    return android.graphics.Color.argb(
        color.alpha,
        color.red,
        color.green,
        color.blue
    )
}