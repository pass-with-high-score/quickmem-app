package com.pwhs.quickmem.presentation.app.classes.detail

import android.content.Intent
import android.widget.Toast
import androidx.annotation.StringRes
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
import com.pwhs.quickmem.core.utils.AppConstant
import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.domain.model.users.ClassMemberModel
import com.pwhs.quickmem.domain.model.users.UserResponseModel
import com.pwhs.quickmem.presentation.app.classes.detail.component.ClassDetailBottomSheet
import com.pwhs.quickmem.presentation.app.classes.detail.component.ClassDetailTopAppBar
import com.pwhs.quickmem.presentation.app.classes.detail.component.InviteClassBottomSheet
import com.pwhs.quickmem.presentation.app.classes.detail.folders.FoldersTabScreen
import com.pwhs.quickmem.presentation.app.classes.detail.members.MembersTabScreen
import com.pwhs.quickmem.presentation.app.classes.detail.study_sets.StudySetsTabScreen
import com.pwhs.quickmem.presentation.app.report.ReportTypeEnum
import com.pwhs.quickmem.presentation.component.LoadingOverlay
import com.pwhs.quickmem.presentation.component.QuickMemAlertDialog
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.AddFolderToClassScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AddStudySetToClassScreenDestination
import com.ramcosta.composedestinations.generated.destinations.EditClassScreenDestination
import com.ramcosta.composedestinations.generated.destinations.FolderDetailScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ReportScreenDestination
import com.ramcosta.composedestinations.generated.destinations.StudySetDetailScreenDestination
import com.ramcosta.composedestinations.generated.destinations.UserDetailScreenDestination
import com.ramcosta.composedestinations.generated.destinations.WelcomeScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.result.ResultRecipient
import com.pwhs.quickmem.R
import com.pwhs.quickmem.presentation.ads.BannerAds

@Composable
@Destination<RootGraph>(
    navArgs = ClassDetailArgs::class
)
fun ClassDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: ClassDetailViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    resultBackNavigator: ResultRecipient<EditClassScreenDestination, Boolean>,
    resultNavigator: ResultBackNavigator<Boolean>,
    resultStudySetDetail: ResultRecipient<StudySetDetailScreenDestination, Boolean>,
    resultFolderDetail: ResultRecipient<FolderDetailScreenDestination, Boolean>,
    resultAddStudySetToClass: ResultRecipient<AddStudySetToClassScreenDestination, Boolean>,
    resultAddFolderToClass: ResultRecipient<AddFolderToClassScreenDestination, Boolean>,
) {

    resultAddFolderToClass.onNavResult { result ->
        when (result) {
            NavResult.Canceled -> {
                // Do nothing
            }

            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(ClassDetailUiAction.Refresh)
                }
            }
        }
    }

    resultAddStudySetToClass.onNavResult { result ->
        when (result) {
            NavResult.Canceled -> {
                // Do nothing
            }

            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(ClassDetailUiAction.Refresh)
                }
            }
        }
    }

    resultBackNavigator.onNavResult { result ->
        when (result) {
            NavResult.Canceled -> {
                // Do nothing
            }

            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(ClassDetailUiAction.Refresh)
                }
            }
        }
    }
    resultStudySetDetail.onNavResult { result ->
        when (result) {
            NavResult.Canceled -> {
                // Do nothing
            }

            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(ClassDetailUiAction.Refresh)
                }
            }
        }
    }


    resultFolderDetail.onNavResult { result ->
        when (result) {
            NavResult.Canceled -> {
                // Do nothing
            }

            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(ClassDetailUiAction.Refresh)
                }
            }
        }
    }

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ClassDetailUiEvent.NavigateToWelcome -> {
                    navigator.navigate(WelcomeScreenDestination) {
                        popUpTo(WelcomeScreenDestination) {
                            inclusive = true
                        }
                    }
                }

                ClassDetailUiEvent.OnJoinClass -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_you_are_member_in_this_class),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ClassDetailUiEvent.ShowError -> {
                    Toast.makeText(context, context.getString(event.message), Toast.LENGTH_SHORT)
                        .show()
                }

                ClassDetailUiEvent.ClassDeleted -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_class_deleted), Toast.LENGTH_SHORT
                    ).show()
                    resultNavigator.navigateBack(true)
                }

                ClassDetailUiEvent.NavigateToEditClass -> {
                    navigator.navigate(
                        EditClassScreenDestination(
                            classId = uiState.id,
                            classTitle = uiState.title,
                            classDescription = uiState.description,
                            isMemberAllowed = uiState.isAllowManage,
                            isSetAllowed = uiState.allowMember
                        )
                    )
                }

                ClassDetailUiEvent.OnNavigateToAddFolder -> {
                    navigator.navigate(AddFolderToClassScreenDestination(classId = uiState.id))
                }

                ClassDetailUiEvent.OnNavigateToAddStudySets -> {
                    navigator.navigate(AddStudySetToClassScreenDestination(classId = uiState.id))
                }

                ClassDetailUiEvent.ExitClass -> {
                    viewModel.onEvent(ClassDetailUiAction.Refresh)
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_exit_class),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                ClassDetailUiEvent.OnNavigateToRemoveMembers -> {

                }

                ClassDetailUiEvent.InviteToClassSuccess -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_invite_to_class_success), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    ClassDetail(
        modifier = modifier,
        linkShareCode = uiState.joinClassCode,
        isOwner = uiState.isOwner,
        isMember = uiState.isMember,
        onRefresh = {
            viewModel.onEvent(ClassDetailUiAction.Refresh)
        },
        onNavigateBack = {
            resultNavigator.navigateBack(true)
        },
        title = uiState.title,
        username = uiState.username,
        errorMessage = uiState.errorMessage,
        onUsernameChanged = {
            viewModel.onEvent(ClassDetailUiAction.OnChangeUsername(it))
        },
        isLoading = uiState.isLoading,
        isAllowMember = uiState.allowMember,
        userResponseModel = uiState.userResponseModel,
        studySets = uiState.studySets,
        folders = uiState.folders,
        members = uiState.members,
        onEditClass = {
            viewModel.onEvent(ClassDetailUiAction.EditClass)
        },
        onDeleteClass = {
            viewModel.onEvent(ClassDetailUiAction.DeleteClass)
        },
        onNavigateAddStudySets = {
            viewModel.onEvent(ClassDetailUiAction.OnNavigateToAddStudySets)
        },
        onNavigateAddFolder = {
            viewModel.onEvent(ClassDetailUiAction.OnNavigateToAddFolder)
        },
        onNavigateToUserDetail = {
            navigator.navigate(
                UserDetailScreenDestination(
                    userId = it,
                )
            )
        },
        onStudySetItemClicked = {
            navigator.navigate(
                StudySetDetailScreenDestination(
                    id = it.id,
                )
            )
        },
        onFolderItemClicked = {
            navigator.navigate(
                FolderDetailScreenDestination(
                    id = it.id,
                )
            )
        },
        onExitClass = {
            viewModel.onEvent(ClassDetailUiAction.ExitClass)
        },
        onRemoveMembers = {
            viewModel.onEvent(ClassDetailUiAction.OnDeleteMember(it))
        },
        onJoinClass = {
            viewModel.onEvent(ClassDetailUiAction.OnJoinClass)
        },
        onInviteClass = {
            viewModel.onEvent(ClassDetailUiAction.OnInviteClass)
        },
        onReportClass = {
            navigator.navigate(
                ReportScreenDestination(
                    reportType = ReportTypeEnum.CLASS,
                    reportedEntityId = uiState.id,
                    ownerOfReportedEntity = uiState.userResponseModel.id
                )
            )
        },
        onDeleteStudySetClick = {
            viewModel.onEvent(ClassDetailUiAction.OnDeleteStudySetInClass(it))
        },
        onDeleteFolderClick = {
            viewModel.onEvent(ClassDetailUiAction.OnDeleteFolderInClass(it))
        },
        isAllowManage = uiState.isAllowManage,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDetail(
    modifier: Modifier = Modifier,
    isOwner: Boolean,
    title: String = "",
    username: String = "",
    onUsernameChanged: (String) -> Unit = {},
    @StringRes errorMessage: Int? = null,
    isLoading: Boolean = false,
    isMember: Boolean = false,
    isAllowMember: Boolean = false,
    isAllowManage: Boolean = false,
    linkShareCode: String = "",
    userResponseModel: UserResponseModel = UserResponseModel(),
    studySets: List<GetStudySetResponseModel> = emptyList(),
    folders: List<GetFolderResponseModel> = emptyList(),
    members: List<ClassMemberModel> = emptyList(),
    onNavigateBack: () -> Unit = {},
    onNavigateAddFolder: () -> Unit = {},
    onNavigateAddStudySets: () -> Unit = {},
    onNavigateToUserDetail: (String) -> Unit = {},
    onEditClass: () -> Unit = {},
    onExitClass: () -> Unit = {},
    onJoinClass: () -> Unit = {},
    onInviteClass: () -> Unit = {},
    onRemoveMembers: (String) -> Unit = {},
    onDeleteClass: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onStudySetItemClicked: (GetStudySetResponseModel) -> Unit = {},
    onFolderItemClicked: (GetFolderResponseModel) -> Unit = {},
    onReportClass: () -> Unit = {},
    onDeleteStudySetClick: (String) -> Unit = {},
    onDeleteFolderClick: (String) -> Unit = {},
) {
    val tabTitles = listOf(
        stringResource(id = R.string.txt_study_sets),
        stringResource(id = R.string.txt_folders),
        stringResource(R.string.txt_members)
    )
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }

    val refreshState = rememberPullToRefreshState()
    var showMoreBottomSheet by remember { mutableStateOf(false) }
    val sheetShowMoreState = rememberModalBottomSheetState()
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    var showExitConfirmationDialog by remember { mutableStateOf(false) }
    var showInviteClassBottomSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            ClassDetailTopAppBar(
                title = title,
                onNavigateBack = onNavigateBack,
                onMoreClicked = { showMoreBottomSheet = true },
                modifier = modifier,
                studySetCount = studySets.size,
                userResponse = userResponseModel,
                onNavigateToUserDetail = {
                    onNavigateToUserDetail(it)
                },
                onShareClicked = {
                    val link = AppConstant.BASE_URL + "class/join/" + linkShareCode
                    val text =
                        context.getString(R.string.txt_join_class_by_link_with_me, title, link)
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, text)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
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
                    TabRow(
                        selectedTabIndex = tabIndex,
                        indicator = { tabPositions ->
                            SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
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
                    when (tabIndex) {
                        ClassDetailEnums.STUDY_SETS.index -> StudySetsTabScreen(
                            modifier = Modifier.fillMaxSize(),
                            onAddStudySetClicked = onNavigateAddStudySets,
                            studySets = studySets,
                            onStudySetItemClicked = onStudySetItemClicked,
                            isOwner = isOwner,
                            onDeleteStudySetClicked = onDeleteStudySetClick
                        )

                        ClassDetailEnums.FOLDERS.index -> FoldersTabScreen(
                            modifier = Modifier.fillMaxSize(),
                            onAddFoldersClicked = onNavigateAddFolder,
                            folders = folders,
                            onFolderItemClicked = onFolderItemClicked,
                            isOwner = isOwner,
                            onDeleteFolderClicked = onDeleteFolderClick
                        )

                        ClassDetailEnums.MEMBERS.index -> MembersTabScreen(
                            modifier = Modifier.fillMaxSize(),
                            members = members,
                            onMembersItemClicked = {
                                onNavigateToUserDetail(it.id)
                            },
                            isOwner = isOwner,
                            onDeletedClicked = {
                                onRemoveMembers(it)
                            }
                        )
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

    if (showDeleteConfirmationDialog) {
        QuickMemAlertDialog(
            onDismissRequest = {
                showDeleteConfirmationDialog = false
                showMoreBottomSheet = true
            },
            onConfirm = {
                onDeleteClass()
                showDeleteConfirmationDialog = false
            },
            title = stringResource(id = R.string.txt_delete_class),
            text = stringResource(R.string.txt_are_you_sure_you_want_to_delete_this_class),
            confirmButtonTitle = stringResource(R.string.txt_delete),
            dismissButtonTitle = stringResource(R.string.txt_cancel),
            buttonColor = colorScheme.error,
        )
    }

    if (showExitConfirmationDialog) {
        QuickMemAlertDialog(
            onDismissRequest = {
                showExitConfirmationDialog = false
            },
            onConfirm = {
                onExitClass()
                showExitConfirmationDialog = false
            },
            title = stringResource(id = R.string.txt_exit_class),
            text = stringResource(R.string.txt_are_you_sure_you_want_to_exit_this_class),
            confirmButtonTitle = stringResource(R.string.txt_exit),
            dismissButtonTitle = stringResource(R.string.txt_cancel),
            buttonColor = colorScheme.error,
        )
    }

    if (showInviteClassBottomSheet) {
        InviteClassBottomSheet(
            username = username,
            errorMessage = errorMessage,
            onUsernameChanged = onUsernameChanged,
            sheetShowMoreState = sheetShowMoreState,
            onDismissRequest = {
                showInviteClassBottomSheet = false
                onUsernameChanged("")
            },
            onSubmitClick = {
                onInviteClass()
            }
        )
    }

    ClassDetailBottomSheet(
        onAddStudySetToClass = onNavigateAddStudySets,
        onAddFolderToClass = onNavigateAddFolder,
        onEditClass = onEditClass,
        onDeleteClass = {
            showDeleteConfirmationDialog = true
            showMoreBottomSheet = false
        },
        onExitClass = {
            showExitConfirmationDialog = true
            showMoreBottomSheet = false
        },
        onInviteClass = {
            showInviteClassBottomSheet = true
            showMoreBottomSheet = false
        },
        onReportClass = {
            onReportClass()
            showMoreBottomSheet = false
        },
        showMoreBottomSheet = showMoreBottomSheet,
        sheetShowMoreState = sheetShowMoreState,
        onDismissRequest = { showMoreBottomSheet = false },
        isOwner = isOwner,
        isMember = isMember,
        isAllowMember = isAllowMember,
        isAllowManage = isAllowManage,
        onJoinClass = {
            onJoinClass()
            showMoreBottomSheet = false
        },
    )
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
private fun ClassDetailScreenPreview() {
    QuickMemTheme {
        ClassDetail(
            title = "Class Title",
            isOwner = false,
        )
    }
}