package com.pwhs.quickmem.presentation.app.study_set.edit

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.domain.model.subject.SubjectModel
import com.pwhs.quickmem.presentation.ads.BannerAds
import com.pwhs.quickmem.presentation.app.study_set.component.StudySetColorInput
import com.pwhs.quickmem.presentation.app.study_set.component.StudySetSubjectBottomSheet
import com.pwhs.quickmem.presentation.app.study_set.component.StudySetSubjectInput
import com.pwhs.quickmem.presentation.component.CreateTextField
import com.pwhs.quickmem.presentation.component.CreateTopAppBar
import com.pwhs.quickmem.presentation.component.LoadingOverlay
import com.pwhs.quickmem.presentation.component.SwitchContainer
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.utils.rememberImeState
import com.pwhs.quickmem.utils.toColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination<RootGraph>(
    navArgs = EditStudySetScreenArgs::class
)
@Composable
fun EditStudySetScreen(
    modifier: Modifier = Modifier,
    viewModel: EditStudySetViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is EditStudySetUiEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is EditStudySetUiEvent.StudySetEdited -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_study_set_edited), Toast.LENGTH_SHORT
                    ).show()
                    resultNavigator.setResult(true)
                    navigator.popBackStack()
                }
            }
        }
    }

    EditStudySet(
        modifier = modifier,
        isLoading = uiState.isLoading,
        title = uiState.title,
        titleError = uiState.titleError,
        onTitleChange = { viewModel.onEvent(EditStudySetUiAction.TitleChanged(it)) },
        description = uiState.description,
        onDescriptionChange = { viewModel.onEvent(EditStudySetUiAction.DescriptionChanged(it)) },
        subjectModel = uiState.subjectModel,
        onSubjectChange = { viewModel.onEvent(EditStudySetUiAction.SubjectChanged(it)) },
        colorModel = uiState.colorModel,
        onColorChange = { viewModel.onEvent(EditStudySetUiAction.ColorChanged(it)) },
        isPublic = uiState.isPublic,
        onIsPublicChange = { viewModel.onEvent(EditStudySetUiAction.PublicChanged(it)) },
        onDoneClick = { viewModel.onEvent(EditStudySetUiAction.SaveClicked) },
        onNavigateBack = { navigator.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStudySet(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    title: String = "",
    @StringRes titleError: Int? = null,
    onTitleChange: (String) -> Unit = {},
    description: String = "",
    descriptionError: String = "",
    onDescriptionChange: (String) -> Unit = {},
    subjectModel: SubjectModel? = SubjectModel.defaultSubjects.first(),
    onSubjectChange: (SubjectModel) -> Unit = {},
    colorModel: ColorModel? = ColorModel.defaultColors.first(),
    onColorChange: (ColorModel) -> Unit = {},
    isPublic: Boolean = true,
    onIsPublicChange: (Boolean) -> Unit = {},
    onDoneClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    val sheetSubjectState = rememberModalBottomSheetState()
    var showBottomSheetEdit by remember {
        mutableStateOf(false)
    }
    var searchSubjectQuery by remember {
        mutableStateOf("")
    }
    val filteredSubjects = SubjectModel.defaultSubjects.filter {
        stringResource(it.subjectName).contains(searchSubjectQuery, ignoreCase = true)
    }
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()
    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue, tween(300))
        }
    }

    Scaffold(
        containerColor = colorScheme.background,
        modifier = modifier,
        topBar = {
            CreateTopAppBar(
                onNavigateBack = onNavigateBack,
                onDoneClick = onDoneClick,
                title = stringResource(R.string.txt_edit_study_set)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .imePadding()
            ) {
                CreateTextField(
                    value = title,
                    title = stringResource(R.string.txt_study_set_title),
                    valueError = titleError?.let { stringResource(it) },
                    onValueChange = onTitleChange,
                    placeholder = stringResource(R.string.txt_enter_study_set_title)
                )
                CreateTextField(
                    value = description,
                    valueError = descriptionError,
                    onValueChange = onDescriptionChange,
                    title = stringResource(R.string.txt_description_optional),
                    placeholder = stringResource(R.string.txt_enter_description)
                )
                StudySetSubjectInput(
                    subjectModel = subjectModel,
                    onShowBottomSheet = {
                        showBottomSheetEdit = true
                    }
                )
                StudySetColorInput(
                    colorModel = colorModel,
                    onColorChange = onColorChange
                )
                SwitchContainer(
                    checked = isPublic,
                    onCheckedChange = onIsPublicChange,
                    text = stringResource(R.string.txt_when_you_make_a_study_set_public_anyone_can_see_it_and_use_it)
                )

            }
            BannerAds(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
            LoadingOverlay(
                isLoading = isLoading,
                color = colorModel?.hexValue?.toColor()
            )
        }

        StudySetSubjectBottomSheet(
            showBottomSheet = showBottomSheetEdit,
            sheetSubjectState = sheetSubjectState,
            searchSubjectQuery = searchSubjectQuery,
            onSearchQueryChange = { searchSubjectQuery = it },
            filteredSubjects = filteredSubjects,
            onSubjectSelected = {
                onSubjectChange(it)
                showBottomSheetEdit = false
            },
            onDismissRequest = { showBottomSheetEdit = false }
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun EditFlashCardScreenPreview() {
    QuickMemTheme {
        EditStudySet()
    }
}