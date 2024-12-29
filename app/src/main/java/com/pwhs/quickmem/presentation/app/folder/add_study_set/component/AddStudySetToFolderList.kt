package com.pwhs.quickmem.presentation.app.folder.add_study_set.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.domain.model.subject.SubjectModel
import com.pwhs.quickmem.domain.model.users.UserResponseModel
import com.pwhs.quickmem.presentation.app.library.component.SearchTextField
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun AddStudySetToFolderList(
    modifier: Modifier = Modifier,
    studySets: List<GetStudySetResponseModel> = emptyList(),
    onAddStudySetToFolder: (String) -> Unit = {},
    studySetImportedIds: List<String> = emptyList(),
    avatarUrl: String = "",
    username: String = "",
) {

    var searchQuery by remember { mutableStateOf("") }

    val filterStudySets = studySets.filter {
        searchQuery.trim().takeIf { query -> query.isNotEmpty() }?.let { query ->
            it.title.contains(query, ignoreCase = true)
        } ?: true
    }

    Box(modifier = modifier) {
        when {
            studySets.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = stringResource(id = R.string.txt_user_avatar),
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = stringResource(R.string.txt_hello, username),
                        style = typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = colorScheme.onSurface.copy(alpha = 0.1f),
                    )
                    Text(
                        text = stringResource(id = R.string.txt_empty_owned_study_sets_prompt),
                        textAlign = TextAlign.Center,
                        style = typography.bodyMedium.copy(
                            color = colorScheme.onSurface.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
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
                            placeholder = stringResource(id = R.string.txt_search_study_sets),
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
                                    text = stringResource(id = R.string.txt_no_study_set_folder_found),
                                    style = typography.bodyLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    items(items = filterStudySets, key = { it.id }) { studySet ->
                        AddStudySetToFolderItem(
                            studySet = studySet,
                            onAddStudySetToFolder = {
                                onAddStudySetToFolder(it)
                            },
                            isAdded = studySetImportedIds.contains(studySet.id)
                        )
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
private fun AddStudySetToFolderListPreview() {
    QuickMemTheme {
        Scaffold {
            AddStudySetToFolderList(
                modifier = Modifier
                    .padding(it),
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


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddStudySetToFolderListPreviewEmpty() {
    QuickMemTheme {
        Scaffold {
            AddStudySetToFolderList(
                modifier = Modifier
                    .padding(it),
                studySets = emptyList()
            )
        }
    }
}