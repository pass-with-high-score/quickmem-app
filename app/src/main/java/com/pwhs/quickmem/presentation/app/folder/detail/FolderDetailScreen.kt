package com.pwhs.quickmem.presentation.app.folder.detail

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.LearnFrom
import com.pwhs.quickmem.core.data.enums.LearnMode
import com.pwhs.quickmem.core.utils.AppConstant
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.presentation.ads.BannerAds
import com.pwhs.quickmem.presentation.app.folder.detail.component.FolderDetailStudySetList
import com.pwhs.quickmem.presentation.app.folder.detail.component.FolderDetailTopAppBar
import com.pwhs.quickmem.presentation.app.folder.detail.component.FolderMenuBottomSheet
import com.pwhs.quickmem.presentation.app.report.ReportTypeEnum
import com.pwhs.quickmem.presentation.app.study_set.detail.material.LearnModeCard
import com.pwhs.quickmem.presentation.component.LoadingOverlay
import com.pwhs.quickmem.presentation.component.QuickMemAlertDialog
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.utils.formatDate
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.AddStudySetToFolderScreenDestination
import com.ramcosta.composedestinations.generated.destinations.EditFolderScreenDestination
import com.ramcosta.composedestinations.generated.destinations.FlipFlashCardScreenDestination
import com.ramcosta.composedestinations.generated.destinations.LearnByQuizScreenDestination
import com.ramcosta.composedestinations.generated.destinations.LearnByTrueFalseScreenDestination
import com.ramcosta.composedestinations.generated.destinations.LearnByWriteScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ReportScreenDestination
import com.ramcosta.composedestinations.generated.destinations.StudySetDetailScreenDestination
import com.ramcosta.composedestinations.generated.destinations.UserDetailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.result.ResultRecipient
import timber.log.Timber

@Destination<RootGraph>(
    navArgs = FolderDetailArgs::class
)
@Composable
fun FolderDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: FolderDetailViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<Boolean>,
    resultEditFolder: ResultRecipient<EditFolderScreenDestination, Boolean>,
    resultStudySetDetail: ResultRecipient<StudySetDetailScreenDestination, Boolean>,
    resultAddStudySet: ResultRecipient<AddStudySetToFolderScreenDestination, Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    resultAddStudySet.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {}
            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(FolderDetailUiAction.Refresh)
                }
            }
        }
    }

    resultEditFolder.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {}
            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(FolderDetailUiAction.Refresh)
                }
            }
        }
    }

    resultStudySetDetail.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {}
            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(FolderDetailUiAction.Refresh)
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                FolderDetailUiEvent.NavigateToEditFolder -> {
                    Timber.d(uiState.id)
                    navigator.navigate(
                        EditFolderScreenDestination(
                            folderId = uiState.id,
                            folderTitle = uiState.title,
                            folderDescription = uiState.description,
                            folderIsPublic = uiState.isPublic
                        )
                    )
                }

                is FolderDetailUiEvent.ShowError -> {
                    Toast.makeText(context, context.getString(event.message), Toast.LENGTH_SHORT)
                        .show()
                }

                FolderDetailUiEvent.FolderDeleted -> {
                    resultNavigator.navigateBack(true)
                }

                is FolderDetailUiEvent.OnNavigateToFlipFlashcard -> {
                    navigator.navigate(
                        FlipFlashCardScreenDestination(
                            studySetId = "",
                            studySetTitle = "",
                            studySetDescription = "",
                            studySetColorId = 1,
                            studySetSubjectId = 1,
                            folderId = uiState.id,
                            learnFrom = LearnFrom.FOLDER,
                            isGetAll = event.isGetAll
                        )
                    )
                }

                is FolderDetailUiEvent.OnNavigateToQuiz -> {
                    navigator.navigate(
                        LearnByQuizScreenDestination(
                            studySetId = "",
                            studySetTitle = "",
                            studySetDescription = "",
                            studySetColorId = 1,
                            studySetSubjectId = 1,
                            folderId = uiState.id,
                            learnFrom = LearnFrom.FOLDER,
                            isGetAll = event.isGetAll
                        )
                    )
                }

                is FolderDetailUiEvent.OnNavigateToTrueFalse -> {
                    navigator.navigate(
                        LearnByTrueFalseScreenDestination(
                            studySetId = "",
                            studySetTitle = "",
                            studySetDescription = "",
                            studySetColorId = 1,
                            studySetSubjectId = 1,
                            folderId = uiState.id,
                            learnFrom = LearnFrom.FOLDER,
                            isGetAll = event.isGetAll
                        )
                    )
                }

                is FolderDetailUiEvent.OnNavigateToWrite -> {
                    navigator.navigate(
                        LearnByWriteScreenDestination(
                            studySetId = "",
                            studySetTitle = "",
                            studySetDescription = "",
                            studySetColorId = 1,
                            studySetSubjectId = 1,
                            folderId = uiState.id,
                            learnFrom = LearnFrom.FOLDER,
                            isGetAll = event.isGetAll
                        )
                    )
                }

                is FolderDetailUiEvent.NotFound -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_folder_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                    resultNavigator.navigateBack(true)
                }

                is FolderDetailUiEvent.UnAuthorized -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_unauthorized), Toast.LENGTH_SHORT
                    ).show()
                    navigator.navigate(NavGraphs.root) {
                        popUpTo(NavGraphs.root) {
                            saveState = false
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                }
            }
        }
    }

    FolderDetail(
        modifier = modifier,
        title = uiState.title,
        isOwner = uiState.isOwner,
        createdAt = uiState.createdAt,
        updatedAt = uiState.updatedAt,
        isLoading = uiState.isLoading,
        linkShareCode = uiState.linkShareCode,
        onFolderRefresh = { viewModel.onEvent(FolderDetailUiAction.Refresh) },
        userAvatar = uiState.user.avatarUrl,
        studySets = uiState.studySets,
        onStudySetClick = {
            navigator.navigate(
                StudySetDetailScreenDestination(
                    id = it,
                )
            )
        },
        onEditFolder = { viewModel.onEvent(FolderDetailUiAction.EditFolder) },
        onNavigateBack = {
            resultNavigator.navigateBack(true)
        },
        onAddStudySet = {
            navigator.navigate(
                AddStudySetToFolderScreenDestination(
                    folderId = uiState.id
                )
            )
        },
        onDeleteFolder = { viewModel.onEvent(FolderDetailUiAction.DeleteFolder) },
        onNavigateToUserDetail = {
            navigator.navigate(
                UserDetailScreenDestination(
                    userId = uiState.user.id
                )
            )
        },
        onReportClick = {
            navigator.navigate(
                ReportScreenDestination(
                    reportType = ReportTypeEnum.FOLDER,
                    reportedEntityId = uiState.id,
                    ownerOfReportedEntity = uiState.user.id
                )
            )
        },
        totalFlashCards = uiState.totalFlashCards,
        onNavigateToLearn = { learnMode, isGetAll ->
            viewModel.onEvent(FolderDetailUiAction.NavigateToLearn(learnMode, isGetAll))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderDetail(
    modifier: Modifier = Modifier,
    title: String = "",
    isOwner: Boolean,
    linkShareCode: String = "",
    createdAt: String = "",
    updatedAt: String = "",
    isLoading: Boolean = false,
    onFolderRefresh: () -> Unit = {},
    studySets: List<GetStudySetResponseModel> = emptyList(),
    userAvatar: String = "",
    onStudySetClick: (String) -> Unit = {},
    onEditFolder: () -> Unit = {},
    onDeleteFolder: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onAddStudySet: () -> Unit = {},
    onNavigateToUserDetail: () -> Unit = {},
    totalFlashCards: Int = 0,
    onReportClick: () -> Unit = {},
    onNavigateToLearn: (LearnMode, Boolean) -> Unit = { _, _ -> },
) {
    val context = LocalContext.current
    val formattedCreatedAt = createdAt.formatDate()
    val formattedUpdatedAt = updatedAt.formatDate()
    val dateLabel = if (createdAt != updatedAt) {
        stringResource(R.string.txt_modified, formattedUpdatedAt)
    } else {
        stringResource(R.string.txt_created, formattedCreatedAt)
    }

    val refreshState = rememberPullToRefreshState()
    var showMoreBottomSheet by remember { mutableStateOf(false) }
    val sheetShowMoreState = rememberModalBottomSheetState()
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    var showStudyFolderBottomSheet by remember { mutableStateOf(false) }
    var showGetAllDialog by remember { mutableStateOf(false) }
    val sheetStudyFolderState = rememberModalBottomSheetState()
    var learningMode by remember { mutableStateOf(LearnMode.NONE) }

    Scaffold(
        modifier = modifier,
        topBar = {
            FolderDetailTopAppBar(
                title = title,
                dateLabel = dateLabel,
                onNavigateBack = onNavigateBack,
                onMoreClicked = { showMoreBottomSheet = true },
                onAddStudySet = onAddStudySet,
                avatarUrl = userAvatar,
                onNavigateToUserDetail = onNavigateToUserDetail,
                isOwner = isOwner
            )
        },
        floatingActionButton = {
            if (isOwner && studySets.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        showStudyFolderBottomSheet = true
                    },
                    containerColor = colorScheme.secondary,
                    contentColor = colorScheme.onSecondary
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = stringResource(R.string.txt_study)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            PullToRefreshBox(
                state = refreshState,
                isRefreshing = isLoading,
                onRefresh = onFolderRefresh
            ) {
                FolderDetailStudySetList(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    studySets = studySets,
                    onStudySetClick = onStudySetClick,
                    onAddFlashCardClick = onAddStudySet,
                    isOwner = isOwner
                )
            }
            BannerAds(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
            LoadingOverlay(
                isLoading = isLoading
            )
        }
    }
    if (showDeleteConfirmationDialog) {
        QuickMemAlertDialog(
            onDismissRequest = {
                showDeleteConfirmationDialog = false
                showMoreBottomSheet = true
            },
            onConfirm = {
                onDeleteFolder()
                showDeleteConfirmationDialog = false
            },
            title = stringResource(R.string.txt_delete_folder),
            text = stringResource(R.string.txt_are_you_sure_you_want_to_delete_this_folder),
            confirmButtonTitle = stringResource(R.string.txt_delete),
            dismissButtonTitle = stringResource(R.string.txt_cancel),
            buttonColor = colorScheme.error,
        )
    }
    FolderMenuBottomSheet(
        onEditFolder = onEditFolder,
        onDeleteFolder = {
            showDeleteConfirmationDialog = true
            showMoreBottomSheet = false
        },
        onShareFolder = {
            val link = AppConstant.BASE_URL + "folder/share/" + linkShareCode
            val text = context.getString(R.string.txt_check_out_this_folder, title, link)
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        },
        showMoreBottomSheet = showMoreBottomSheet,
        sheetShowMoreState = sheetShowMoreState,
        onDismissRequest = { showMoreBottomSheet = false },
        isOwner = isOwner,
        onReportClick = onReportClick
    )
    if (showStudyFolderBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showStudyFolderBottomSheet = false },
            sheetState = sheetStudyFolderState,
            containerColor = colorScheme.surface,
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LearnModeCard(
                    title = stringResource(R.string.txt_flip_flashcards),
                    icon = R.drawable.ic_flipcard,
                    containerColor = colorScheme.surface,
                    elevation = 0.dp,
                    onClick = {
                        showGetAllDialog = true
                        learningMode = LearnMode.FLIP
                        showStudyFolderBottomSheet = false
                    },
                )
                LearnModeCard(
                    title = stringResource(R.string.txt_quiz),
                    icon = R.drawable.ic_quiz,
                    containerColor = colorScheme.surface,
                    elevation = 0.dp,
                    onClick = {
                        showGetAllDialog = true
                        learningMode = LearnMode.QUIZ
                        showStudyFolderBottomSheet = false
                    }
                )
                LearnModeCard(
                    title = stringResource(R.string.txt_true_false),
                    icon = R.drawable.ic_tf,
                    containerColor = colorScheme.surface,
                    elevation = 0.dp,
                    onClick = {
                        showGetAllDialog = true
                        learningMode = LearnMode.TRUE_FALSE
                        showStudyFolderBottomSheet = false
                    }
                )
                LearnModeCard(
                    title = stringResource(R.string.txt_write),
                    icon = R.drawable.ic_write,
                    containerColor = Color.Transparent,
                    elevation = 0.dp,
                    onClick = {
                        showGetAllDialog = true
                        learningMode = LearnMode.WRITE
                        showStudyFolderBottomSheet = false
                    }
                )
            }
        }
    }
    if (showGetAllDialog && totalFlashCards > 10) {
        QuickMemAlertDialog(
            onDismissRequest = {
                showGetAllDialog = false
                onNavigateToLearn(learningMode, true)
                learningMode = LearnMode.NONE
            },
            onConfirm = {
                showGetAllDialog = false
                onNavigateToLearn(learningMode, false)
                learningMode = LearnMode.NONE
            },
            title = stringResource(R.string.txt_get_all),
            text = stringResource(R.string.txt_are_you_sure_you_want_to_get_all_flashcards),
            confirmButtonTitle = stringResource(R.string.txt_ok),
            dismissButtonTitle = stringResource(R.string.txt_no_thanks),
        )
    } else {
        onNavigateToLearn(learningMode, true)
        learningMode = LearnMode.NONE
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
private fun FolderDetailPreview() {
    QuickMemTheme {
        FolderDetail(
            title = "Folder Title",
            isOwner = true
        )
    }
}