package com.pwhs.quickmem.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ActionButtonTopAppBar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    showBadge: Boolean = false,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(end = 8.dp)
            .background(color = Color.White, shape = CircleShape)
            .border(
                width = 2.dp,
                color = colorScheme.primary,
                shape = CircleShape
            )
            .padding(8.dp)
            .clip(CircleShape)
            .clickable {
                onClick()
            }
    ) {
        content()
        if (showBadge) {
            Badge(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(y = 4.dp, x = (-4).dp)
                    .size(8.dp),
                contentColor = colorScheme.error
            )
        }
    }
}