package com.pwhs.quickmem.presentation.app.flashcard.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.domain.model.flashcard.VoiceModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceBottomSheet(
    modifier: Modifier = Modifier,
    isTerm: Boolean = true,
    voiceModel: VoiceModel?,
    voiceList: List<VoiceModel> = emptyList(),
    onDismissRequest: () -> Unit,
    onVoiceSelected: (VoiceModel, Boolean) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Select Voice",
                style = MaterialTheme.typography.titleMedium
            )
            LazyColumn {
                items(voiceList) { voice ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onVoiceSelected(voice, isTerm)
                                onDismissRequest()
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = voiceModel == voice,
                            onClick = {
                                onVoiceSelected(voice, isTerm)
                                onDismissRequest()
                            }
                        )
                        Text(
                            text = voice.name,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp)
                                .weight(1f)
                        )

                        Text(
                            text = voice.gender,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}