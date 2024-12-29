package com.pwhs.quickmem.presentation.app.classes.edit

import android.widget.Toast
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.presentation.ads.BannerAds
import com.pwhs.quickmem.presentation.component.CreateTextField
import com.pwhs.quickmem.presentation.component.CreateTopAppBar
import com.pwhs.quickmem.presentation.component.LoadingOverlay
import com.pwhs.quickmem.presentation.component.SwitchContainer
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.util.rememberImeState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination<RootGraph>(
    navArgs = EditClassScreenArgs::class
)

@Composable
fun EditClassScreen(
    modifier: Modifier = Modifier,
    viewModel: EditClassViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                EditClassUiEvent.ClassesUpdated -> {
                    resultNavigator.navigateBack(true)
                }

                is EditClassUiEvent.ShowError -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_update_class_failed), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    EditClass(
        modifier = modifier,
        isLoading = uiState.isLoading,
        title = uiState.title,
        description = uiState.description,
        allowSetManagement = uiState.allowSetManagement,
        allowMemberManagement = uiState.allowMemberManagement,
        onNavigateBack = {
            resultNavigator.navigateBack(false)
        },
        onDoneClick = {
            viewModel.onEvent(EditClassUiAction.SaveClicked)
        },
        onAllowMemberManagementChange = {
            viewModel.onEvent(EditClassUiAction.OnAllowMemberChanged(it))
        },
        onAllowSetManagementChange = {
            viewModel.onEvent(EditClassUiAction.OnAllowSetChanged(it))
        },
        onTitleChange = {
            viewModel.onEvent(EditClassUiAction.TitleChanged(it))
        },
        onDescriptionChange = {
            viewModel.onEvent(EditClassUiAction.DescriptionChanged(it))
        }
    )

}

@Composable
fun EditClass(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onDoneClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    title: String = "",
    titleError: String = "",
    onTitleChange: (String) -> Unit = {},
    description: String = "",
    descriptionError: String = "",
    onDescriptionChange: (String) -> Unit = {},
    allowSetManagement: Boolean = false,
    onAllowSetManagementChange: (Boolean) -> Unit = {},
    allowMemberManagement: Boolean = false,
    onAllowMemberManagementChange: (Boolean) -> Unit = {},
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
                title = stringResource(R.string.txt_edit_this_class)
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
                    valueError = titleError,
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
                    text = stringResource(R.string.txt_allow_class_members_to_send_invites_to_other_people),
                    checked = allowMemberManagement,
                    onCheckedChange = onAllowMemberManagementChange
                )
                SwitchContainer(
                    text = stringResource(R.string.txt_allow_class_members_to_add_study_set_and_folders),
                    checked = allowSetManagement,
                    onCheckedChange = onAllowSetManagementChange
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
private fun EditClassPreview() {
    QuickMemTheme {
        EditClass()
    }
}