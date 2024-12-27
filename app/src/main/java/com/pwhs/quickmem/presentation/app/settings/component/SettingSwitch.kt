package com.pwhs.quickmem.presentation.app.settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun SettingSwitch(
    title: String,
    subtitle: String? = null,
    value: Boolean = false,
    onChangeValue: (Boolean) -> Unit = {},
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Medium)
            subtitle?.let {
                Text(
                    text = it,
                    style = typography.bodyMedium.copy(
                        color = colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                )
            }
        }
        Switch(
            checked = value,
            onCheckedChange = onChangeValue
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingSwitchPreview() {
    QuickMemTheme {
        SettingSwitch(title = "Title", subtitle = "Subtitle")
    }

}
