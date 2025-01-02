package com.pwhs.quickmem.presentation.app.study_set.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.utils.toColor

@Composable
fun StudySetColorInput(
    modifier: Modifier = Modifier,
    colorModel: ColorModel?,
    onColorChange: (ColorModel) -> Unit
) {
    Column(
        modifier = modifier.padding(top = 10.dp)
    ) {
        Text(
            text = stringResource(R.string.txt_color),
            style = typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .border(
                    width = 1.dp,
                    color = colorModel?.hexValue?.toColor() ?: colorScheme.onSurface.copy(
                        alpha = 0.12f
                    ),
                    shape = RoundedCornerShape(8.dp)
                ),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyRow(
                modifier = Modifier
                    .padding(8.dp)
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                items(ColorModel.defaultColors) { color ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (colorModel?.hexValue == color.hexValue) {
                                    color.hexValue
                                        .toColor()
                                        .copy(alpha = 0.5f)
                                } else {
                                    Color.Transparent
                                }
                            )
                            .border(2.dp, Color.Transparent, CircleShape)
                            .padding(4.dp)
                            .clickable {
                                onColorChange(color)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(23.dp)
                                .clip(CircleShape)
                                .background(color.hexValue.toColor()),
                            contentAlignment = Alignment.Center
                        ) {
                            this@Row.AnimatedVisibility(
                                visible = colorModel?.hexValue == color.hexValue,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_done),
                                    contentDescription = "Selected",
                                    modifier = Modifier.size(16.dp),
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}