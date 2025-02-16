package com.pwhs.quickmem.presentation.app.flashcard.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.domain.model.flashcard.LanguageModel
import com.pwhs.quickmem.presentation.app.library.component.SearchTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageBottomSheet(
    modifier: Modifier = Modifier,
    isTerm: Boolean = true,
    languageModel: LanguageModel?,
    languageList: List<LanguageModel> = emptyList(),
    onDismissRequest: () -> Unit,
    onLanguageSelected: (LanguageModel, Boolean) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState()

    var query by remember { mutableStateOf("") }

    val filteredLanguageList = languageList.filter {
        it.name.contains(query, ignoreCase = true) || it.country.contains(query, ignoreCase = true)
    }

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
                text = "Select Language",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    SearchTextField(
                        searchQuery = query,
                        onSearchQueryChange = { query = it },
                    )
                }
                item {
                    if (filteredLanguageList.isEmpty()) {
                        Text(
                            text = "No language found",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
                items(filteredLanguageList) { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                onLanguageSelected(language, isTerm)
                                onDismissRequest()
                            },
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = language.code == languageModel?.code,
                            onClick = {
                                onLanguageSelected(language, isTerm)
                                onDismissRequest()
                            }
                        )
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = language.name,
                            )
                            Row {
                                Text(
                                    text = language.country,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                )
                                Text(
                                    text = language.flag,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                )
                            }
                        }

                        Text(
                            text = "Voice: ${language.voiceAvailableCount}",
                        )
                    }
                }
            }
        }
    }
}