package com.pwhs.quickmem.presentation.app.folder.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.domain.model.subject.SubjectModel
import com.pwhs.quickmem.domain.model.users.UserResponseModel
import com.pwhs.quickmem.presentation.app.library.component.SearchTextField
import com.pwhs.quickmem.presentation.app.library.study_set.component.StudySetItem
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun FolderDetailStudySetList(
    isOwner: Boolean,
    modifier: Modifier = Modifier,
    studySets: List<GetStudySetResponseModel> = emptyList(),
    onStudySetClick: (String) -> Unit = {},
    onAddFlashCardClick: () -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }

    val filterStudySets = studySets.filter {
        searchQuery.trim().takeIf { query -> query.isNotEmpty() }?.let { query ->
            it.title.contains(query, ignoreCase = true)
        } != false
    }
    Box(modifier = modifier) {
        when {
            studySets.isEmpty() -> {
                Column(
                    horizontalAlignment = CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 100.dp)
                ) {
                    Text(
                        text = stringResource(R.string.txt_the_folder_has_no_study_sets),
                        style = typography.titleLarge.copy(
                            fontWeight = Bold,
                            color = colorScheme.onSurface
                        ),
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                    if (isOwner) {
                        Button(
                            onClick = onAddFlashCardClick
                        ) {
                            Row(
                                verticalAlignment = CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = stringResource(R.string.txt_add_folders),
                                    tint = colorScheme.background,
                                )
                                Text(
                                    text = stringResource(R.string.txt_add_study_set),
                                    style = typography.titleMedium.copy(
                                        color = colorScheme.background,
                                        fontWeight = Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    horizontalAlignment = CenterHorizontally,
                ) {
                    item {
                        SearchTextField(
                            searchQuery = searchQuery,
                            onSearchQueryChange = { searchQuery = it },
                            placeholder = stringResource(R.string.txt_search_study_sets),
                        )
                    }
                    items(items = filterStudySets, key = { it.id }) { studySet ->
                        StudySetItem(
                            studySet = studySet,
                            onStudySetClick = { onStudySetClick(studySet.id) }
                        )
                    }
                    item {
                        if (filterStudySets.isEmpty() && searchQuery.trim().isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(R.string.txt_no_study_sets_found),
                                    style = typography.bodyLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.padding(60.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ListStudySetInnerFolderPreview() {
    QuickMemTheme {
        Scaffold {
            FolderDetailStudySetList(
                modifier = Modifier
                    .padding(it),
                isOwner = true,
                studySets = listOf(
                    GetStudySetResponseModel(
                        id = "1",
                        title = "Study Set 1",
                        flashcardCount = 10,
                        color = ColorModel.defaultColors[0],
                        subject = SubjectModel.defaultSubjects[0],
                        owner = UserResponseModel(
                            id = "1",

                            ),
                        description = "Description",
                        isPublic = true,
                        linkShareCode = "123",
                        flashcards = emptyList(),
                        createdAt = "2021-01-01",
                        updatedAt = "2021-01-01",
                        isAIGenerated = false
                    )
                )
            )
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun ListStudySetInnerFolderPreviewEmpty() {
    QuickMemTheme {
        Scaffold {
            FolderDetailStudySetList(
                modifier = Modifier
                    .padding(it),
                studySets = emptyList(),
                isOwner = true
            )
        }
    }
}