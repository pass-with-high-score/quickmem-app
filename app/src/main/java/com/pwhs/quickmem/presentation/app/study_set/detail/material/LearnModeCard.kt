package com.pwhs.quickmem.presentation.app.study_set.detail.material

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun LearnModeCard(
    modifier: Modifier = Modifier,
    title: String = "",
    @DrawableRes icon: Int,
    onClick: () -> Unit = {},
    color: Color = colorScheme.primary,
    leadingText: String = "",
    learningPercentage: Int? = null,
    containerColor: Color = Color.White,
    contentColor: Color = colorScheme.onSurface,
    elevation: Dp = 5.dp
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = elevation,
            pressedElevation = elevation
        ),
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 10.dp),
        border = BorderStroke(
            1.dp,
            when (learningPercentage) {
                in 0..25 -> color.copy(alpha = 0.2f)
                in 26..50 -> color.copy(alpha = 0.5f)
                in 51..75 -> color.copy(alpha = 0.8f)
                in 76..100 -> color
                else -> Color.Transparent

            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(16.dp),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = stringResource(R.string.txt_flip_card),
                modifier = Modifier
                    .size(30.dp),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(color)
            )
            Text(
                text = title,
                style = typography.titleMedium.copy(
                    color = colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1f)
            )
            if (learningPercentage != null) {
                Text(
                    text = "$learningPercentage%",
                    style = typography.bodyMedium.copy(
                        color = colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            if (leadingText.isNotEmpty()) {
                Text(
                    text = leadingText,
                    style = typography.bodyMedium.copy(
                        color = colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LearnModeCardPreview() {
    QuickMemTheme {
        LearnModeCard(
            title = "Flip Flashcards",
            icon = R.drawable.ic_flipcard,
            leadingText = "Completed",
            learningPercentage = 10
        )
    }
}