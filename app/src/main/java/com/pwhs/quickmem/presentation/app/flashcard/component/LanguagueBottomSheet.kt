package com.pwhs.quickmem.presentation.app.flashcard.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.domain.model.flashcard.LanguageModel
import com.pwhs.quickmem.domain.model.flashcard.VoiceModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageBottomSheet(
    modifier: Modifier = Modifier,
    languageModel: LanguageModel? = null,
    voiceModel: VoiceModel? = null,
    languageList: List<LanguageModel> = emptyList(),
    voiceList: List<VoiceModel> = emptyList(),
    onDismissRequest: () -> Unit,
    onLanguageSelected: (LanguageModel) -> Unit,
    onVoiceSelected: (VoiceModel) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState,
    ) {
        Column {
            Text(
                text = if (languageModel == null && voiceModel == null) "Select Language" else "Select Voice",
                modifier = Modifier.padding(16.dp)
            )
            if (languageModel == null && voiceModel == null) {
                LazyColumn {
                    items(languageList) { language ->
                        Text(
                            text = language.name,
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    onLanguageSelected(language)
                                }
                        )
                    }
                }
            } else {
                LazyColumn {
                    items(voiceList) { voice ->
                        Text(
                            text = voice.name,
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    onVoiceSelected(voice)
                                    onDismissRequest()
                                }
                        )
                    }
                }
            }
        }
    }
}