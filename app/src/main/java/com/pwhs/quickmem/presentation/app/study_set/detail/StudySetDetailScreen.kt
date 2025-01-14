package com.pwhs.quickmem.presentation.app.study_set.detail

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.FlipCardStatus
import com.pwhs.quickmem.core.data.enums.LearnFrom
import com.pwhs.quickmem.core.data.enums.LearnMode
import com.pwhs.quickmem.core.data.enums.QuizStatus
import com.pwhs.quickmem.core.data.enums.TrueFalseStatus
import com.pwhs.quickmem.core.data.enums.WriteStatus
import com.pwhs.quickmem.core.utils.AppConstant
import com.pwhs.quickmem.domain.model.flashcard.StudySetFlashCardResponseModel
import com.pwhs.quickmem.domain.model.study_time.GetStudyTimeByStudySetResponseModel
import com.pwhs.quickmem.domain.model.users.UserResponseModel
import com.pwhs.quickmem.presentation.ads.BannerAds
import com.pwhs.quickmem.presentation.app.report.ReportTypeEnum
import com.pwhs.quickmem.presentation.app.study_set.detail.component.StudySetDetailTopAppBar
import com.pwhs.quickmem.presentation.app.study_set.detail.component.StudySetMoreOptionsBottomSheet
import com.pwhs.quickmem.presentation.app.study_set.detail.material.MaterialTabScreen
import com.pwhs.quickmem.presentation.app.study_set.detail.progress.ProgressTabScreen
import com.pwhs.quickmem.presentation.components.LoadingOverlay
import com.pwhs.quickmem.presentation.components.QuickMemAlertDialog
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.AddStudySetToClassesScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AddStudySetToFoldersScreenDestination
import com.ramcosta.composedestinations.generated.destinations.CreateFlashCardScreenDestination
import com.ramcosta.composedestinations.generated.destinations.EditFlashCardScreenDestination
import com.ramcosta.composedestinations.generated.destinations.EditStudySetScreenDestination
import com.ramcosta.composedestinations.generated.destinations.FlipFlashCardScreenDestination
import com.ramcosta.composedestinations.generated.destinations.LearnByQuizScreenDestination
import com.ramcosta.composedestinations.generated.destinations.LearnByTrueFalseScreenDestination
import com.ramcosta.composedestinations.generated.destinations.LearnByWriteScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ReportScreenDestination
import com.ramcosta.composedestinations.generated.destinations.StudySetDetailScreenDestination
import com.ramcosta.composedestinations.generated.destinations.StudySetInfoScreenDestination
import com.ramcosta.composedestinations.generated.destinations.UserDetailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.result.ResultRecipient

@Destination<RootGraph>(
    navArgs = StudySetDetailArgs::class
)
@Composable
fun StudySetDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: StudySetDetailViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<Boolean>,
    resultBakFlashCard: ResultRecipient<CreateFlashCardScreenDestination, Boolean>,
    resultEditStudySet: ResultRecipient<EditStudySetScreenDestination, Boolean>,
    resultEditFlashCard: ResultRecipient<EditFlashCardScreenDestination, Boolean>,
    resultFlipFlashCard: ResultRecipient<FlipFlashCardScreenDestination, Boolean>,
    resultQuiz: ResultRecipient<LearnByQuizScreenDestination, Boolean>,
    resultWrite: ResultRecipient<LearnByWriteScreenDestination, Boolean>,
    resultTrueFalse: ResultRecipient<LearnByTrueFalseScreenDestination, Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    resultBakFlashCard.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {}
            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(StudySetDetailUiAction.Refresh)
                }
            }
        }
    }
    resultEditStudySet.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {}
            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(StudySetDetailUiAction.Refresh)
                }
            }
        }
    }

    resultEditFlashCard.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {}
            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(StudySetDetailUiAction.Refresh)
                }
            }
        }
    }

    resultFlipFlashCard.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {}
            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(StudySetDetailUiAction.Refresh)
                }
            }
        }
    }

    resultQuiz.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {}
            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(StudySetDetailUiAction.Refresh)
                }
            }
        }
    }

    resultWrite.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {}
            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(StudySetDetailUiAction.Refresh)
                }
            }
        }
    }

    resultTrueFalse.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {}
            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(StudySetDetailUiAction.Refresh)
                }
            }
        }
    }


    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is StudySetDetailUiEvent.FlashCardDeleted -> {
                    viewModel.onEvent(StudySetDetailUiAction.Refresh)
                }

                is StudySetDetailUiEvent.FlashCardStarred -> {
                    viewModel.onEvent(StudySetDetailUiAction.Refresh)
                }

                is StudySetDetailUiEvent.NavigateToEditStudySet -> {
                    navigator.navigate(
                        EditStudySetScreenDestination(
                            studySetId = uiState.id,
                            studySetTitle = uiState.title,
                            studySetDescription = uiState.description,
                            studySetSubjectId = uiState.subject.id,
                            studySetColorId = uiState.colorModel.id,
                            studySetIsPublic = uiState.isPublic
                        )
                    )
                }

                is StudySetDetailUiEvent.NavigateToEditFlashCard -> {
                    val flashCard =
                        uiState.flashCards.find { it.id == uiState.idOfFlashCardSelected }
                    if (flashCard == null) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.txt_flashcard_not_found), Toast.LENGTH_SHORT
                        ).show()
                        return@collect
                    }
                    navigator.navigate(
                        EditFlashCardScreenDestination(
                            flashcardId = flashCard.id,
                            term = flashCard.term,
                            definition = flashCard.definition,
                            definitionImageUrl = flashCard.definitionImageURL ?: "",
                            hint = flashCard.hint ?: "",
                            explanation = flashCard.explanation ?: "",
                            studySetColorId = uiState.colorModel.id
                        )
                    )
                }

                is StudySetDetailUiEvent.StudySetDeleted -> {
                    resultNavigator.navigateBack(true)
                }

                is StudySetDetailUiEvent.StudySetProgressReset -> {
                    viewModel.onEvent(StudySetDetailUiAction.Refresh)
                }

                is StudySetDetailUiEvent.StudySetCopied -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_study_set_copied), Toast.LENGTH_SHORT
                    ).show()
                    navigator.navigate(
                        StudySetDetailScreenDestination(
                            id = event.newStudySetId,
                        )
                    )
                }

                is StudySetDetailUiEvent.OnNavigateToFlipFlashcard -> {
                    navigator.navigate(
                        FlipFlashCardScreenDestination(
                            studySetId = uiState.id,
                            studySetTitle = uiState.title,
                            studySetDescription = uiState.description,
                            studySetColorId = uiState.colorModel.id,
                            studySetSubjectId = uiState.subject.id,
                            folderId = "",
                            learnFrom = LearnFrom.STUDY_SET,
                            isGetAll = event.isGetAll
                        )
                    )
                }

                is StudySetDetailUiEvent.OnNavigateToQuiz -> {
                    navigator.navigate(
                        LearnByQuizScreenDestination(
                            studySetId = uiState.id,
                            studySetTitle = uiState.title,
                            studySetDescription = uiState.description,
                            studySetColorId = uiState.colorModel.id,
                            studySetSubjectId = uiState.subject.id,
                            folderId = "",
                            learnFrom = LearnFrom.STUDY_SET,
                            isGetAll = event.isGetAll
                        )
                    )
                }

                is StudySetDetailUiEvent.OnNavigateToTrueFalse -> {
                    navigator.navigate(
                        LearnByTrueFalseScreenDestination(
                            studySetId = uiState.id,
                            studySetTitle = uiState.title,
                            studySetDescription = uiState.description,
                            studySetColorId = uiState.colorModel.id,
                            studySetSubjectId = uiState.subject.id,
                            folderId = "",
                            learnFrom = LearnFrom.STUDY_SET,
                            isGetAll = event.isGetAll
                        )
                    )
                }

                is StudySetDetailUiEvent.OnNavigateToWrite -> {
                    navigator.navigate(
                        LearnByWriteScreenDestination(
                            studySetId = uiState.id,
                            studySetTitle = uiState.title,
                            studySetDescription = uiState.description,
                            studySetColorId = uiState.colorModel.id,
                            studySetSubjectId = uiState.subject.id,
                            folderId = "",
                            learnFrom = LearnFrom.STUDY_SET,
                            isGetAll = event.isGetAll
                        )
                    )
                }

                is StudySetDetailUiEvent.NotFound -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_study_set_not_found), Toast.LENGTH_SHORT
                    ).show()
                    resultNavigator.navigateBack(false)
                }

                is StudySetDetailUiEvent.UnAuthorized -> {
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
    StudySetDetail(
        modifier = modifier,
        onNavigateBack = {
            resultNavigator.navigateBack(true)
        },
        onAddFlashcard = {
            navigator.navigate(
                CreateFlashCardScreenDestination(
                    studySetId = uiState.id,
                    studySetColorId = uiState.colorModel.id
                )
            )
        },
        isLoading = uiState.isLoading,
        title = uiState.title,
        color = uiState.color,
        flashCardCount = uiState.flashCardCount,
        flashCards = uiState.flashCards,
        userResponse = uiState.user,
        studyTime = uiState.studyTime,
        onFlashCardClick = { id ->
            viewModel.onEvent(StudySetDetailUiAction.OnIdOfFlashCardSelectedChanged(id))
        },
        onDeleteFlashCard = {
            viewModel.onEvent(StudySetDetailUiAction.OnDeleteFlashCardClicked)
        },
        onAddToClass = {
            navigator.navigate(
                AddStudySetToClassesScreenDestination(
                    studySetId = uiState.id
                )
            )
        },
        onAddToFolder = {
            navigator.navigate(
                AddStudySetToFoldersScreenDestination(
                    studySetId = uiState.id
                )
            )
        },
        onEditFlashCard = {
            viewModel.onEvent(StudySetDetailUiAction.OnEditFlashCardClicked)
        },
        onToggleStarredFlashCard = { id, isStarred ->
            viewModel.onEvent(StudySetDetailUiAction.OnStarFlashCardClicked(id, isStarred))
        },
        onEditStudySet = {
            viewModel.onEvent(StudySetDetailUiAction.OnEditStudySetClicked)
        },
        onNavigateToStudySetInfo = {
            navigator.navigate(
                StudySetInfoScreenDestination(
                    title = uiState.title,
                    isAIGenerated = uiState.isAIGenerated,
                    description = uiState.description,
                    isPublic = uiState.isPublic,
                    authorUsername = uiState.user.username,
                    authorAvatarUrl = uiState.user.avatarUrl,
                    creationDate = uiState.createdAt
                )
            )
        },
        linkShareCode = uiState.linkShareCode,
        onDeleteStudySet = {
            viewModel.onEvent(StudySetDetailUiAction.OnDeleteStudySetClicked)
        },
        onResetProgress = {
            viewModel.onEvent(StudySetDetailUiAction.OnResetProgressClicked(uiState.id))
        },
        isAIGenerated = uiState.isAIGenerated,
        onRefresh = {
            viewModel.onEvent(StudySetDetailUiAction.Refresh)
        },
        onNavigateToUserDetail = {
            navigator.navigate(
                UserDetailScreenDestination(
                    userId = uiState.user.id,
                )
            )
        },
        isOwner = uiState.isOwner,
        onCopyStudySet = {
            viewModel.onEvent(StudySetDetailUiAction.OnMakeCopyClicked)
        },
        onReportClick = {
            navigator.navigate(
                ReportScreenDestination(
                    reportType = ReportTypeEnum.STUDY_SET,
                    reportedEntityId = uiState.id,
                    ownerOfReportedEntity = uiState.user.id
                )
            )
        },
        onNavigateToLearn = { learnMode, isGetAll ->
            viewModel.onEvent(StudySetDetailUiAction.NavigateToLearn(learnMode, isGetAll))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySetDetail(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isOwner: Boolean,
    onNavigateBack: () -> Unit = {},
    onAddFlashcard: () -> Unit = {},
    title: String = "",
    linkShareCode: String = "",
    color: Color = Color.Blue,
    flashCardCount: Int = 0,
    isAIGenerated: Boolean = false,
    studyTime: GetStudyTimeByStudySetResponseModel? = null,
    userResponse: UserResponseModel = UserResponseModel(),
    flashCards: List<StudySetFlashCardResponseModel> = emptyList(),
    onFlashCardClick: (String) -> Unit = {},
    onDeleteFlashCard: () -> Unit = {},
    onEditFlashCard: () -> Unit = {},
    onNavigateToStudySetInfo: () -> Unit = {},
    onToggleStarredFlashCard: (String, Boolean) -> Unit = { _, _ -> },
    onEditStudySet: () -> Unit = {},
    onAddToClass: () -> Unit = {},
    onAddToFolder: () -> Unit = {},
    onDeleteStudySet: () -> Unit = {},
    onResetProgress: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onNavigateToUserDetail: () -> Unit = {},
    onCopyStudySet: () -> Unit = {},
    onReportClick: () -> Unit = {},
    onNavigateToLearn: (LearnMode, Boolean) -> Unit = { _, _ -> },
) {
    val context = LocalContext.current
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabTitles = if (isOwner) {
        listOf(
            stringResource(R.string.txt_material),
            stringResource(R.string.txt_progress)
        )
    } else {
        listOf(
            stringResource(R.string.txt_material)
        )
    }
    var showMoreBottomSheet by remember { mutableStateOf(false) }
    val sheetShowMoreState = rememberModalBottomSheetState()
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    var showResetProgressDialog by remember { mutableStateOf(false) }
    val refreshState = rememberPullToRefreshState()
    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            StudySetDetailTopAppBar(
                title = title,
                color = color,
                userResponse = userResponse,
                flashCardCount = flashCardCount,
                onNavigateBack = onNavigateBack,
                onAddFlashcard = onAddFlashcard,
                onNavigateToUserDetail = onNavigateToUserDetail,
                onMoreClicked = { showMoreBottomSheet = true },
                onShareClicked = {
                    val link = AppConstant.BASE_URL + "study-set/share/" + linkShareCode
                    val text = context.getString(R.string.txt_check_out_this_study_set, title, link)
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, text)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                },
                isOwner = isOwner,
                isAIGenerated = isAIGenerated
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(innerPadding)
        ) {
            PullToRefreshBox(
                onRefresh = onRefresh,
                isRefreshing = isLoading,
                state = refreshState
            ) {
                Column {
                    if (isOwner) {
                        TabRow(
                            selectedTabIndex = tabIndex,
                            indicator = { tabPositions ->
                                SecondaryIndicator(
                                    Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                                    color = color,
                                )
                            },
                            contentColor = colorScheme.onSurface,
                        ) {
                            tabTitles.forEachIndexed { index, title ->
                                Tab(
                                    text = {
                                        Text(
                                            title, style = typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (tabIndex == index) Color.Black else Color.Gray
                                            )
                                        )
                                    },
                                    selected = tabIndex == index,
                                    onClick = { tabIndex = index },
                                )
                            }
                        }
                    }
                    if (isOwner) {
                        when (tabIndex) {
                            StudySetDetailEnum.MATERIAL.index -> MaterialTabScreen(
                                flashCards = flashCards,
                                onFlashcardClick = onFlashCardClick,
                                onDeleteFlashCardClick = onDeleteFlashCard,
                                onToggleStarClick = onToggleStarredFlashCard,
                                onEditFlashCardClick = onEditFlashCard,
                                onAddFlashCardClick = onAddFlashcard,
                                onNavigateToLearn = onNavigateToLearn,
                                isOwner = true,
                                studySetColor = color,
                                learningPercentQuiz = if (flashCardCount > 0) (flashCards.count { it.quizStatus == QuizStatus.CORRECT.status } * 100 / flashCardCount) else 0,
                                learningPercentFlipped = if (flashCardCount > 0) (flashCards.count { it.flipStatus == FlipCardStatus.KNOW.name } * 100 / flashCardCount) else 0,
                                learningPercentWrite = if (flashCardCount > 0) (flashCards.count { it.writeStatus == WriteStatus.CORRECT.status } * 100 / flashCardCount) else 0,
                                learningPercentTrueFalse = if (flashCardCount > 0) (flashCards.count { it.trueFalseStatus == TrueFalseStatus.CORRECT.status } * 100 / flashCardCount) else 0,
                                onMakeCopyClick = onCopyStudySet,
                            )

                            StudySetDetailEnum.PROGRESS.index -> ProgressTabScreen(
                                modifier = Modifier.fillMaxSize(),
                                totalStudySet = flashCardCount,
                                color = color,
                                studyTime = studyTime,
                                studySetsNotLearnCount = flashCards.count { it.flipStatus == FlipCardStatus.NONE.name },
                                studySetsStillLearningCount = flashCards.count { it.flipStatus == FlipCardStatus.STILL_LEARNING.name },
                                studySetsKnowCount = flashCards.count { it.flipStatus == FlipCardStatus.KNOW.name },
                            )
                        }
                    } else {
                        when (tabIndex) {
                            StudySetDetailEnum.MATERIAL.index -> MaterialTabScreen(
                                flashCards = flashCards,
                                onFlashcardClick = onFlashCardClick,
                                onDeleteFlashCardClick = onDeleteFlashCard,
                                onToggleStarClick = onToggleStarredFlashCard,
                                onEditFlashCardClick = onEditFlashCard,
                                onAddFlashCardClick = onAddFlashcard,
                                onNavigateToLearn = onNavigateToLearn,
                                isOwner = false,
                                onMakeCopyClick = onCopyStudySet,
                                studySetColor = color,
                            )
                        }
                    }

                }
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
    StudySetMoreOptionsBottomSheet(
        onEditStudySet = onEditStudySet,
        onDeleteStudySet = {
            showDeleteConfirmationDialog = true
        },
        onAddToClass = onAddToClass,
        onAddToFolder = onAddToFolder,
        showMoreBottomSheet = showMoreBottomSheet,
        sheetShowMoreState = sheetShowMoreState,
        onDismissRequest = { showMoreBottomSheet = false },
        onInfoStudySet = onNavigateToStudySetInfo,
        onResetProgress = {
            showResetProgressDialog = true
            showMoreBottomSheet = false
        },
        isOwner = isOwner,
        onCopyStudySet = {
            onCopyStudySet()
            showMoreBottomSheet = false
        },
        onReportClick = {
            onReportClick()
            showMoreBottomSheet = false
        }
    )
    if (showDeleteConfirmationDialog) {
        QuickMemAlertDialog(
            onDismissRequest = { showDeleteConfirmationDialog = false },
            onConfirm = {
                onDeleteStudySet()
                showDeleteConfirmationDialog = false
            },
            title = stringResource(R.string.txt_delete_study_set),
            text = stringResource(R.string.txt_are_you_sure_you_want_to_delete_this_study_set),
            confirmButtonTitle = stringResource(R.string.txt_delete),
            dismissButtonTitle = stringResource(R.string.txt_cancel),
            buttonColor = colorScheme.error,
        )
    }

    if (showResetProgressDialog) {
        QuickMemAlertDialog(
            onDismissRequest = { showResetProgressDialog = false },
            onConfirm = {
                onResetProgress()
                showResetProgressDialog = false
            },
            title = stringResource(R.string.txt_reset_progress),
            text = stringResource(R.string.txt_are_you_sure_you_want_to_reset_the_progress_of_this_study_set),
            confirmButtonTitle = stringResource(R.string.txt_reset),
            dismissButtonTitle = stringResource(R.string.txt_cancel),
            buttonColor = Color.Red
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun StudySetDetailScreenPreview() {
    StudySetDetail(isOwner = false)
}