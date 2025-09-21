package com.pwhs.quickmem.presentation.app.folder.edit

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
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.presentation.ads.BannerAds
import com.pwhs.quickmem.presentation.components.CreateTextField
import com.pwhs.quickmem.presentation.components.CreateTopAppBar
import com.pwhs.quickmem.presentation.components.LoadingOverlay
import com.pwhs.quickmem.presentation.components.SwitchContainer
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.utils.rememberImeState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.result.ResultBackNavigator


@Destination<RootGraph>(
    navArgs = EditFolderScreenArgs::class
)
@Composable
fun EditFolderScreen(
    modifier: Modifier = Modifier,
    viewModel: EditFolderViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is EditFolderUiEvent.FolderEdited -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.folder_edited), Toast.LENGTH_SHORT
                    ).show()
                    resultNavigator.navigateBack(true)
                }

                is EditFolderUiEvent.ShowError -> {
                    Toast.makeText(context, context.getString(event.message), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    EditFolder(
        modifier = modifier,
        isLoading = uiState.isLoading,
        title = uiState.title,
        titleError = uiState.titleError,
        onTitleChange = { viewModel.onEvent(EditFolderUiAction.TitleChanged(it)) },
        description = uiState.description,
        onDescriptionChange = { viewModel.onEvent(EditFolderUiAction.DescriptionChanged(it)) },
        isPublic = uiState.isPublic,
        onIsPublicChange = { viewModel.onEvent(EditFolderUiAction.IsPublicChanged(it)) },
        onDoneClick = {
            viewModel.onEvent(EditFolderUiAction.SaveClicked)
        },
        onNavigateBack = {
            resultNavigator.navigateBack(false)
        }
    )

}

@Composable
fun EditFolder(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    title: String = "",
    @StringRes titleError: Int? = null,
    onTitleChange: (String) -> Unit = {},
    description: String = "",
    descriptionError: String = "",
    onDescriptionChange: (String) -> Unit = {},
    isPublic: Boolean = false,
    onIsPublicChange: (Boolean) -> Unit = {},
    onDoneClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
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
                title = stringResource(R.string.txt_edit_folder)
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
                    title = stringResource(R.string.txt_folder_title),
                    valueError = titleError?.let { stringResource(it) },
                    onValueChange = onTitleChange,
                    placeholder = stringResource(R.string.txt_enter_folder_title)
                )
                CreateTextField(
                    value = description,
                    title = stringResource(R.string.txt_description_optional),
                    valueError = descriptionError,
                    onValueChange = onDescriptionChange,
                    placeholder = stringResource(R.string.txt_enter_description)
                )
                SwitchContainer(
                    text = stringResource(R.string.txt_when_you_make_a_folder_public_anyone_can_see_it_and_use_it),
                    checked = isPublic,
                    onCheckedChange = onIsPublicChange
                )
            }

            BannerAds(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )

            LoadingOverlay(
                isLoading = isLoading
            )
        }
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
fun CreateFolderScreenPreview() {
    QuickMemTheme {
        EditFolder()
    }
}