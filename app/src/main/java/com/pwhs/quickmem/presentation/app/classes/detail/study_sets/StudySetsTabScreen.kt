package com.pwhs.quickmem.presentation.app.classes.detail.study_sets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.presentation.app.classes.detail.component.ClassDetailEmpty
import com.pwhs.quickmem.presentation.app.library.study_set.component.StudySetItem
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun StudySetsTabScreen(
    modifier: Modifier = Modifier,
    studySets: List<GetStudySetResponseModel> = emptyList(),
    onAddSetsClicked: () -> Unit = {},
    onStudyCardClicked: (String) -> Unit = {},
) {
    Scaffold { innerPadding ->
        Box(
            modifier = modifier
                .padding(top = 10.dp)
                .fillMaxSize()
        ) {
            when {
                studySets.isEmpty() -> {
                    ClassDetailEmpty(
                        modifier = Modifier.padding(innerPadding),
                        title = "This class has no sets",
                        subtitle = "Add flashcard sets to share them with your class.",
                        buttonTitle = "Add study sets",
                        onAddMembersClicked = onAddSetsClicked
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(studySets) { studySet ->
                            StudySetItem(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                studySet = studySet,
                                onStudySetClick = { onStudyCardClicked(studySet.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun StudySetsTabPreview() {
    QuickMemTheme {
        StudySetsTabScreen()
    }
}