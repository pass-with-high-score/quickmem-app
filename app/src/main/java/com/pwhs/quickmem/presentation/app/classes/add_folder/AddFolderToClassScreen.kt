package com.pwhs.quickmem.presentation.app.classes.add_folder

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.presentation.app.classes.add_folder.component.AddFolderToClassList
import com.pwhs.quickmem.presentation.component.AddItemsTopAppBar
import com.pwhs.quickmem.presentation.component.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CreateFolderScreenDestination
import com.ramcosta.composedestinations.generated.destinations.FolderDetailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.result.ResultRecipient

@Destination<RootGraph>(
    navArgs = AddFolderToClassArgs::class
)
@Composable
fun AddFolderToClassScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: AddFolderToClassViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>,
    resultAddFolderToClass: ResultRecipient<FolderDetailScreenDestination, Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    resultAddFolderToClass.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {
                // Do nothing
            }

            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(AddFolderToClassUiAction.RefreshFolders)
                }
            }
        }

    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AddFolderToClassUiEvent.ShowError -> {
                    Toast.makeText(context, context.getString(event.message), Toast.LENGTH_SHORT)
                        .show()
                }

                AddFolderToClassUiEvent.StudySetAddedToClass -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_add_folder_to_class_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    resultNavigator.setResult(true)
                    navigator.navigateUp()
                }
            }
        }
    }

    AddFolderToClass(
        modifier = modifier,
        isLoading = uiState.isLoading,
        folders = uiState.folders,
        folderImportedIds = uiState.folderImportedIds,
        userAvatar = uiState.userAvatar,
        username = uiState.username,
        onDoneClick = {
            viewModel.onEvent(AddFolderToClassUiAction.AddFolderToClass)
        },
        onNavigateCancel = {
            navigator.navigateUp()
        },
        onCreateFolderToClassClick = {
            navigator.navigate(
                CreateFolderScreenDestination()
            )
        },
        onAddFolderToClass = {
            viewModel.onEvent(AddFolderToClassUiAction.ToggleFolderImport(it))
        }
    )
}

@Composable
fun AddFolderToClass(
    modifier: Modifier = Modifier,
    onDoneClick: () -> Unit = {},
    onNavigateCancel: () -> Unit = {},
    onCreateFolderToClassClick: () -> Unit = {},
    folders: List<GetFolderResponseModel> = emptyList(),
    folderImportedIds: List<String> = emptyList(),
    userAvatar: String = "",
    username: String = "",
    onAddFolderToClass: (String) -> Unit = {},
    isLoading: Boolean = false,
) {
    Scaffold(
        containerColor = colorScheme.background,
        modifier = modifier,
        topBar = {
            AddItemsTopAppBar(
                onDoneClick = onDoneClick,
                onNavigateCancel = onNavigateCancel,
                title = stringResource(R.string.txt_add_folder)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateFolderToClassClick,
                containerColor = colorScheme.secondary,
                contentColor = colorScheme.onSecondary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.txt_create_folder)
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
                AddFolderToClassList(
                    modifier = modifier,
                    folders = folders,
                    folderImportedIds = folderImportedIds,
                    onAddFolderToClass = onAddFolderToClass,
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
fun AddFolderToClassPreview() {
    QuickMemTheme {
        AddFolderToClass(
            folders = emptyList()
        )
    }
}