package com.pwhs.quickmem.presentation.app.classes.add_study_set

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.presentation.app.classes.add_study_set.component.AddStudySetToClassList
import com.pwhs.quickmem.presentation.component.AddItemsTopAppBar
import com.pwhs.quickmem.presentation.component.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CreateStudySetScreenDestination
import com.ramcosta.composedestinations.generated.destinations.StudySetDetailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.result.ResultRecipient

@Destination<RootGraph>(
    navArgs = AddStudySetToClassArgs::class
)
@Composable
fun AddStudySetToClassScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: AddStudySetToClassViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>,
    resultAddStudySetToClass: ResultRecipient<StudySetDetailScreenDestination, Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    resultAddStudySetToClass.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {
                // Do nothing
            }

            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(AddStudySetToClassUiAction.RefreshStudySets)
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AddStudySetToClassUiEvent.Error -> {
                    Toast.makeText(context, context.getString(event.message), Toast.LENGTH_SHORT)
                        .show()
                }

                is AddStudySetToClassUiEvent.StudySetAddedToClass -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_add_study_set_to_class_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    resultNavigator.setResult(true)
                    navigator.navigateUp()
                }
            }
        }
    }
    AddStudySetToClass(
        modifier = modifier,
        isLoading = uiState.isLoading,
        studySets = uiState.studySets,
        userAvatar = uiState.userAvatar,
        username = uiState.username,
        studySetImportedIds = uiState.studySetImportedIds,
        onDoneClick = {
            viewModel.onEvent(AddStudySetToClassUiAction.AddStudySetToClass)
        },
        onNavigateCancel = {
            resultNavigator.setResult(true)
            navigator.navigateUp()
        },
        onCreateStudySetToClassClick = {
            navigator.navigate(
                CreateStudySetScreenDestination()
            )
        },
        onAddStudySetToClass = {
            viewModel.onEvent(AddStudySetToClassUiAction.ToggleStudySetImport(it))
        }
    )
}

@Composable
fun AddStudySetToClass(
    modifier: Modifier = Modifier,
    studySets: List<GetStudySetResponseModel> = emptyList(),
    isLoading: Boolean = false,
    userAvatar: String = "",
    username: String = "",
    studySetImportedIds: List<String> = emptyList(),
    onDoneClick: () -> Unit = {},
    onNavigateCancel: () -> Unit = {},
    onCreateStudySetToClassClick: () -> Unit = {},
    onAddStudySetToClass: (String) -> Unit = {},
) {
    Scaffold(
        containerColor = colorScheme.background,
        modifier = modifier,
        topBar = {
            AddItemsTopAppBar(
                onDoneClick = onDoneClick,
                onNavigateCancel = onNavigateCancel,
                title = stringResource(R.string.txt_add_study_set)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateStudySetToClassClick,
                containerColor = colorScheme.secondary,
                contentColor = colorScheme.onSecondary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.txt_create_study_set)
                )
            }
        }
    ) { innerPadding ->
        Box {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                AddStudySetToClassList(
                    modifier = modifier,
                    studySets = studySets,
                    studySetImportedIds = studySetImportedIds,
                    onAddStudySetToClass = onAddStudySetToClass,
                    avatarUrl = userAvatar,
                    username = username,
                )
            }
        }
        LoadingOverlay(isLoading = isLoading)
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
private fun AddStudySetPreview() {
    QuickMemTheme {
        AddStudySetToClass()
    }
}