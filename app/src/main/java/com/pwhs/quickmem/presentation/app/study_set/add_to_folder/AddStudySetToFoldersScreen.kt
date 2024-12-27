package com.pwhs.quickmem.presentation.app.study_set.add_to_folder

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
import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.presentation.app.study_set.add_to_folder.component.AddStudySetToFoldersList
import com.pwhs.quickmem.presentation.component.LoadingOverlay
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CreateFolderScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.pwhs.quickmem.R
import com.pwhs.quickmem.presentation.component.AddItemsTopAppBar
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.ramcosta.composedestinations.generated.destinations.FolderDetailScreenDestination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient

@Destination<RootGraph>(
    navArgs = AddStudySetToFoldersArgs::class
)
@Composable
fun AddStudySetToFoldersScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: AddStudySetToFoldersViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>,
    resultAddStudySetToFolders: ResultRecipient<FolderDetailScreenDestination, Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    resultAddStudySetToFolders.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {
                // Do nothing
            }

            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(AddStudySetToFoldersUiAction.RefreshFolders)
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AddStudySetToFoldersUiEvent.Error -> {
                    Toast.makeText(context, context.getString(event.message), Toast.LENGTH_SHORT)
                        .show()
                }

                is AddStudySetToFoldersUiEvent.StudySetAddedToFolders -> {
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
    AddStudySetToFolders(
        modifier = modifier,
        isLoading = uiState.isLoading,
        folders = uiState.folders,
        folderImportedIds = uiState.folderImportedIds,
        userAvatar = uiState.userAvatar,
        username = uiState.username,
        onDoneClick = {
            viewModel.onEvent(AddStudySetToFoldersUiAction.AddStudySetToFolders)
        },
        onNavigateCancel = {
            navigator.navigateUp()
        },
        onCreateFolderClick = {
            navigator.navigate(
                CreateFolderScreenDestination()
            )
        },
        onAddStudySetToFolders = {
            viewModel.onEvent(AddStudySetToFoldersUiAction.ToggleStudySetImport(it))
        }
    )
}

@Composable
fun AddStudySetToFolders(
    modifier: Modifier = Modifier,
    onDoneClick: () -> Unit = {},
    onNavigateCancel: () -> Unit = {},
    onCreateFolderClick: () -> Unit = {},
    folders: List<GetFolderResponseModel> = emptyList(),
    folderImportedIds: List<String> = emptyList(),
    userAvatar: String = "",
    username: String = "",
    onAddStudySetToFolders: (String) -> Unit = {},
    isLoading: Boolean = false,
) {
    Scaffold(
        containerColor = colorScheme.background,
        modifier = modifier,
        topBar = {
            AddItemsTopAppBar(
                onDoneClick = onDoneClick,
                onNavigateCancel = onNavigateCancel,
                title = stringResource(R.string.txt_add_to_folder)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateFolderClick,
                containerColor = colorScheme.secondary,
                contentColor = colorScheme.onSecondary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.txt_add_folder)
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
                AddStudySetToFoldersList(
                    modifier = modifier,
                    folders = folders,
                    folderImportedIds = folderImportedIds,
                    onAddStudySetToFolders = onAddStudySetToFolders,
                    avatarUrl = userAvatar,
                    username = username,
                )
            }
        }
        LoadingOverlay(isLoading = isLoading)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AddStudySetToFolderPreview() {
    QuickMemTheme {
        AddStudySetToFolders()
    }
}