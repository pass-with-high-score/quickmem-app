package com.pwhs.quickmem.presentation.onboarding.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun OnboardingButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    showIcon: Boolean = false,
    backgroundColor: Color = colorScheme.primary,
    buttonShape: CornerBasedShape = shapes.large,
    borderColor: Color = Color.White,
    textColor: Color = Color.White,
) {
    Button(
        onClick = onClick,
        colors = buttonColors(
            containerColor = backgroundColor,
        ),
        modifier = modifier
            .height(50.dp),
        shape = buttonShape,
        border = BorderStroke(1.dp, borderColor),
        elevation = buttonElevation(
            defaultElevation = 10.dp,
            pressedElevation = 10.dp,
            focusedElevation = 10.dp,
            hoveredElevation = 10.dp,
            disabledElevation = 10.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = text,
                style = TextStyle(
                    color = textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
            if (showIcon) {
                Icon(
                    imageVector = AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingButtonPreview() {
    QuickMemTheme {
        OnboardingButton(
            text = "Next",
            onClick = {},
            showIcon = true
        )
    }
}