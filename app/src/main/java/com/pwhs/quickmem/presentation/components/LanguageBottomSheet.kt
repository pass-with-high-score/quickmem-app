package com.pwhs.quickmem.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.LanguageCode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    language: String,
    onLanguageChange: (String) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clickable {
                        onLanguageChange(
                            if (language == LanguageCode.VI.code) {
                                LanguageCode.EN.code
                            } else {
                                LanguageCode.VI.code
                            }
                        )
                        onDismissRequest()
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_vn_flag),
                        contentDescription = stringResource(R.string.txt_vn_flag),
                        modifier = Modifier
                            .size(24.dp)
                    )

                    Text(
                        text = stringResource(R.string.txt_vietnamese),
                        style = typography.bodyMedium.copy(
                            color = colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .padding(start = 10.dp)
                    )
                }
                if (language == LanguageCode.VI.code) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.txt_check),
                        tint = colorScheme.primary
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clickable {
                        onLanguageChange(LanguageCode.EN.code)
                        onDismissRequest()
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_us_flag),
                        contentDescription = stringResource(R.string.txt_us_flag),
                        modifier = Modifier
                            .size(24.dp)
                    )

                    Text(
                        text = stringResource(R.string.txt_english_us),
                        style = typography.bodyMedium.copy(
                            color = colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .padding(start = 10.dp)
                    )
                }

                if (language == LanguageCode.EN.code) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.txt_check),
                        tint = colorScheme.primary
                    )
                }
            }
        }
    }
}