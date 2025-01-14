package com.pwhs.quickmem.presentation.app.study_set.add_to_class

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
import com.pwhs.quickmem.domain.model.classes.GetClassByOwnerResponseModel
import com.pwhs.quickmem.presentation.app.study_set.add_to_class.component.AddStudySetToClassesList
import com.pwhs.quickmem.presentation.components.AddItemsTopAppBar
import com.pwhs.quickmem.presentation.components.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ClassDetailScreenDestination
import com.ramcosta.composedestinations.generated.destinations.CreateFolderScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.result.ResultRecipient

@Destination<RootGraph>(
    navArgs = AddStudySetToClassesArgs::class
)
@Composable
fun AddStudySetToClassesScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: AddStudySetToClassesViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>,
    resultAddStudySetToClasses: ResultRecipient<ClassDetailScreenDestination, Boolean>
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    resultAddStudySetToClasses.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {
                // Do nothing
            }

            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(AddStudySetToClassesUiAction.RefreshClasses)
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AddStudySetToClassesUiEvent.Error -> {
                    Toast.makeText(context, context.getString(event.message), Toast.LENGTH_SHORT)
                        .show()
                }

                is AddStudySetToClassesUiEvent.StudySetAddedToClasses -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_add_to_class_success), Toast.LENGTH_SHORT
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
        classes = uiState.classes,
        classImportedIds = uiState.classImportedIds,
        onDoneClick = {
            viewModel.onEvent(AddStudySetToClassesUiAction.AddStudySetToClasses)
        },
        onNavigateCancel = {
            navigator.navigateUp()
        },
        onCreateFolderClick = {
            navigator.navigate(
                CreateFolderScreenDestination()
            )
        },
        onAddStudySetToClasses = {
            viewModel.onEvent(AddStudySetToClassesUiAction.ToggleStudySetImport(it))
        },
        onRefresh = {
            viewModel.onEvent(AddStudySetToClassesUiAction.RefreshClasses)
        }
    )
}

@Composable
fun AddStudySetToFolders(
    modifier: Modifier = Modifier,
    onDoneClick: () -> Unit = {},
    onNavigateCancel: () -> Unit = {},
    onCreateFolderClick: () -> Unit = {},
    classes: List<GetClassByOwnerResponseModel> = emptyList(),
    classImportedIds: List<String> = emptyList(),
    onAddStudySetToClasses: (String) -> Unit = {},
    isLoading: Boolean = false,
    onRefresh: () -> Unit = {}
) {
    Scaffold(
        containerColor = colorScheme.background,
        modifier = modifier,
        topBar = {
            AddItemsTopAppBar(
                onDoneClick = onDoneClick,
                onNavigateCancel = onNavigateCancel,
                title = stringResource(R.string.txt_add_to_classes)
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
                AddStudySetToClassesList(
                    modifier = modifier,
                    classes = classes,
                    classImportedIds = classImportedIds,
                    onAddStudySetToClasses = onAddStudySetToClasses,
                    onClassRefresh = onRefresh,
                )
            }
            LoadingOverlay(isLoading = isLoading)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AddStudySetToFolderPreview() {
    QuickMemTheme {
        AddStudySetToFolders()
    }
}