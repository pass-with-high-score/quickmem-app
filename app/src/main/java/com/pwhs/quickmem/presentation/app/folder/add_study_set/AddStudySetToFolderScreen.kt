package com.pwhs.quickmem.presentation.app.folder.add_study_set

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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.presentation.app.folder.add_study_set.component.AddStudySetToFolderList
import com.pwhs.quickmem.presentation.components.LoadingOverlay
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CreateStudySetScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.pwhs.quickmem.R
import com.pwhs.quickmem.presentation.components.AddItemsTopAppBar
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.ramcosta.composedestinations.generated.destinations.StudySetDetailScreenDestination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient

@Destination<RootGraph>(
    navArgs = AddStudySetToFolderArgs::class
)
@Composable
fun AddStudySetToFolderScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: AddStudySetToFolderViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>,
    resultAddStudySetToFolder: ResultRecipient<StudySetDetailScreenDestination, Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    resultAddStudySetToFolder.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {
                // Do nothing
            }

            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(AddStudySetToFolderUiAction.RefreshStudySets)
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AddStudySetToFolderUiEvent.Error -> {
                    Toast.makeText(context, context.getString(event.message), Toast.LENGTH_SHORT)
                        .show()
                }

                is AddStudySetToFolderUiEvent.StudySetAddedToFolder -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_add_study_set_to_folder_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    resultNavigator.setResult(true)
                    navigator.navigateUp()
                }
            }
        }
    }
    AddStudySetToFolder(
        modifier = modifier,
        isLoading = uiState.isLoading,
        studySets = uiState.studySets,
        userAvatar = uiState.userAvatar,
        username = uiState.username,
        studySetImportedIds = uiState.studySetImportedIds,
        onDoneClick = {
            viewModel.onEvent(AddStudySetToFolderUiAction.AddStudySetToFolder)
        },
        onNavigateCancel = {
            resultNavigator.setResult(true)
            navigator.navigateUp()
        },
        onCreateStudySetClick = {
            navigator.navigate(
                CreateStudySetScreenDestination()
            )
        },
        onAddStudySetToFolder = {
            viewModel.onEvent(AddStudySetToFolderUiAction.ToggleStudySetImport(it))
        }
    )
}

@Composable
fun AddStudySetToFolder(
    modifier: Modifier = Modifier,
    studySets: List<GetStudySetResponseModel> = emptyList(),
    isLoading: Boolean = false,
    userAvatar: String = "",
    username: String = "",
    studySetImportedIds: List<String> = emptyList(),
    onDoneClick: () -> Unit = {},
    onNavigateCancel: () -> Unit = {},
    onCreateStudySetClick: () -> Unit = {},
    onAddStudySetToFolder: (String) -> Unit = {},
) {
    Scaffold(
        containerColor = colorScheme.background,
        modifier = modifier,
        topBar = {
            AddItemsTopAppBar(
                onDoneClick = onDoneClick,
                onNavigateCancel = onNavigateCancel,
                title = stringResource(id = R.string.txt_add_study_set)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateStudySetClick,
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
                AddStudySetToFolderList(
                    modifier = modifier,
                    studySets = studySets,
                    studySetImportedIds = studySetImportedIds,
                    onAddStudySetToFolder = onAddStudySetToFolder,
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
private fun AddStudySetToFolderPreview() {
    QuickMemTheme {
        AddStudySetToFolder()
    }
}