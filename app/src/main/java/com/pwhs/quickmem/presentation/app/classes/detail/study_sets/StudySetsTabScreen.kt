package com.pwhs.quickmem.presentation.app.classes.detail.study_sets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.presentation.app.classes.detail.component.ClassDetailEmptyItems
import com.pwhs.quickmem.presentation.app.library.component.SearchTextField
import com.pwhs.quickmem.presentation.app.library.study_set.component.StudySetItem
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun StudySetsTabScreen(
    modifier: Modifier = Modifier,
    isOwner: Boolean = false,
    studySets: List<GetStudySetResponseModel> = emptyList(),
    onAddStudySetClicked: () -> Unit = {},
    onStudySetItemClicked: (GetStudySetResponseModel) -> Unit = {},
    onDeleteStudySetClicked: (String) -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }

    val filterStudySets = studySets.filter {
        searchQuery.trim().takeIf { query -> query.isNotEmpty() }?.let { query ->
            it.title.contains(query, ignoreCase = true)
        } != false
    }
    when {
        studySets.isEmpty() -> {
            ClassDetailEmptyItems(
                modifier = modifier,
                title = stringResource(R.string.txt_this_class_has_no_study_sets),
                subtitle = stringResource(R.string.txt_add_flashcard_sets_to_share_them_with_your_class),
                buttonTitle = stringResource(R.string.txt_add_study_sets),
                onAddClick = onAddStudySetClicked,
                isOwner = isOwner
            )
        }

        else -> {
            LazyColumn(
                modifier = modifier,
            ) {
                item {
                    SearchTextField(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        searchQuery = searchQuery,
                        onSearchQueryChange = { searchQuery = it },
                        placeholder = stringResource(R.string.txt_search_study_sets),
                    )
                }
                items(items = filterStudySets, key = { it.id }) { studySet ->
                    StudySetItem(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        studySet = studySet,
                        onStudySetClick = { onStudySetItemClicked(studySet) },
                        isOwner = isOwner,
                        onDeleteClick = { studySetId ->
                            onDeleteStudySetClicked(studySetId)
                        }
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


@Preview(showSystemUi = true)
@Composable
fun StudySetsTabPreview() {
    QuickMemTheme {
        Scaffold { innerPadding ->
            StudySetsTabScreen(
                isOwner = true,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}