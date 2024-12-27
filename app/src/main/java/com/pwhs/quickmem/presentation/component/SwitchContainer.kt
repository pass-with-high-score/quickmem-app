package com.pwhs.quickmem.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun SwitchContainer(
    modifier: Modifier = Modifier,
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        shape = RoundedCornerShape(10.dp),
        colors = cardColors(
            containerColor = colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = text,
                    style = typography.bodySmall.copy(
                        color = colorScheme.onSurface.copy(alpha = 0.6f),
                        fontSize = 14.sp
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp, end = 10.dp)
                        .weight(1f)
                )
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                )
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
private fun SwitchContainerPreview() {
    QuickMemTheme {
        SwitchContainer(
            text = "Allow class members to add study set and folders",
            checked = true,
            onCheckedChange = {}
        )
    }
}