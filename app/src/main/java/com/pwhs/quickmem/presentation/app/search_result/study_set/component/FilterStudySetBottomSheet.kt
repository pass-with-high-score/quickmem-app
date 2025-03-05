package com.pwhs.quickmem.presentation.app.search_result.study_set.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.domain.model.subject.SubjectModel
import com.pwhs.quickmem.presentation.app.search_result.component.TopBarSearch
import com.pwhs.quickmem.presentation.app.search_result.study_set.enums.SearchResultSizeEnum
import com.pwhs.quickmem.presentation.app.study_set.component.StudySetColorInput
import com.pwhs.quickmem.presentation.app.study_set.component.StudySetSubjectBottomSheet
import com.pwhs.quickmem.presentation.app.study_set.component.StudySetSubjectInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterStudySetBottomSheet(
    modifier: Modifier = Modifier,
    colorModel: ColorModel? = ColorModel.defaultColors.first(),
    onColorChange: (ColorModel) -> Unit = {},
    onSubjectChange: (SubjectModel) -> Unit = {},
    subjectModel: SubjectModel? = SubjectModel.defaultSubjects.first(),
    sizeModel: SearchResultSizeEnum = SearchResultSizeEnum.ALL,
    onSizeChange: (SearchResultSizeEnum) -> Unit,
    isAiGenerated: Boolean = false,
    onIsAiGeneratedChange: (Boolean) -> Unit,
    onNavigateBack: () -> Unit,
    onResetClick: () -> Unit,
    onApplyClick: () -> Unit,
) {
    val sheetSubjectState = rememberModalBottomSheetState()
    var showBottomSheetCreate by remember { mutableStateOf(false) }
    var searchSubjectQuery by remember { mutableStateOf("") }

    val filteredSubjects = SubjectModel.defaultSubjects.filter {
        stringResource(it.subjectName).contains(searchSubjectQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopBarSearch(
                title = stringResource(R.string.txt_filters),
                onNavigateBack = onNavigateBack,
                onResetClick = onResetClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                FilterSection(
                    title = stringResource(R.string.txt_number_of_terms),
                    options = SearchResultSizeEnum.entries.map { it.title },
                    selectedOption = sizeModel.title,
                    onOptionSelected = { selectedContent ->
                        val selectedSize =
                            SearchResultSizeEnum.entries.first { it.title == selectedContent }
                        onSizeChange(selectedSize)
                    }
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.txt_ai_generated),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Switch(
                        checked = isAiGenerated,
                        onCheckedChange = { onIsAiGeneratedChange(it) },
                    )
                }
            }

            item {
                StudySetSubjectInput(
                    subjectModel = subjectModel,
                    onShowBottomSheet = {
                        showBottomSheetCreate = true
                    }
                )
            }
            item {
                StudySetColorInput(
                    colorModel = colorModel,
                    onColorChange = onColorChange
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                Button(
                    onClick = onApplyClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(50.dp),
                ) {
                    Text(
                        text = stringResource(R.string.txt_apply),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
    StudySetSubjectBottomSheet(
        showBottomSheet = showBottomSheetCreate,
        sheetSubjectState = sheetSubjectState,
        searchSubjectQuery = searchSubjectQuery,
        onSearchQueryChange = { searchSubjectQuery = it },
        filteredSubjects = filteredSubjects,
        onSubjectSelected = {
            onSubjectChange(it)
            showBottomSheetCreate = false
        },
        onDismissRequest = { showBottomSheetCreate = false }
    )

}
