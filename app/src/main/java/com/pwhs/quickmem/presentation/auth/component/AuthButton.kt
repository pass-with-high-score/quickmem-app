package com.pwhs.quickmem.presentation.auth.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun AuthButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    colors: Color = colorScheme.primary,
    borderColor: Color = Color.Transparent,
    textColor: Color = Color.White,
    text: String,
    isEnable: Boolean = true,
    @DrawableRes icon: Int? = null,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    ElevatedButton(
        onClick = {
            focusManager.clearFocus()
            keyboardController?.hide()
            onClick()
        },
        enabled = isEnable,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = buttonColors(
            containerColor = colors
        ),
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        ),
        elevation = buttonElevation(
            defaultElevation = 5.dp,
            pressedElevation = 5.dp,
            focusedElevation = 5.dp,
            hoveredElevation = 5.dp,
            disabledElevation = 5.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            icon?.let {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                )
            }
            Text(
                text, style = typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = textColor
                )
            )

        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AuthButtonPreview() {
    QuickMemTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            AuthButton(
                onClick = {},
                text = "Sign in",
                modifier = Modifier.padding(bottom = 8.dp)
            )
            AuthButton(
                onClick = {},
                text = "Google",
                textColor = Color.Black,
                icon = R.drawable.ic_google,
                colors = Color.White
            )
        }
    }
}