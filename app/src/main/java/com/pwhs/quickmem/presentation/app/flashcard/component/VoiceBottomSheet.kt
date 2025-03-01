package com.pwhs.quickmem.presentation.app.flashcard.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.flashcard.VoiceModel
import com.pwhs.quickmem.presentation.app.library.component.SearchTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceBottomSheet(
    modifier: Modifier = Modifier,
    voiceModel: VoiceModel?,
    voiceList: List<VoiceModel> = emptyList(),
    onDismissRequest: () -> Unit,
    onVoiceSelected: (VoiceModel) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState()

    var query by remember { mutableStateOf("") }

    val filteredVoiceList = voiceList.filter {
        it.name.contains(query, ignoreCase = true) || it.code.contains(query, ignoreCase = true)
    }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(0.95f)
        ) {
            Text(
                text = stringResource(R.string.txt_select_voice),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            LazyColumn {
                item {
                    SearchTextField(
                        searchQuery = query,
                        onSearchQueryChange = { query = it },
                    )
                }
                item {
                    if (filteredVoiceList.isEmpty()) {
                        Text(
                            text = stringResource(R.string.txt_no_voice_found),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
                items(filteredVoiceList) { voice ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onVoiceSelected(voice)
                                onDismissRequest()
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = voiceModel == voice,
                            onClick = {
                                onVoiceSelected(voice)
                                onDismissRequest()
                            }
                        )
                        Text(
                            text = voice.name,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(start = 8.dp)
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