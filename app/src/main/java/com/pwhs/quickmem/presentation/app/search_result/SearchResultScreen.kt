package com.pwhs.quickmem.presentation.app.search_result

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.domain.model.subject.SubjectModel
import com.pwhs.quickmem.domain.model.users.SearchUserResponseModel
import com.pwhs.quickmem.presentation.ads.BannerAds
import com.pwhs.quickmem.presentation.app.search_result.all_result.ListAllResultScreen
import com.pwhs.quickmem.presentation.app.search_result.component.SearchResultEnum
import com.pwhs.quickmem.presentation.app.search_result.component.TopBarSearchResult
import com.pwhs.quickmem.presentation.app.search_result.folder.ListResultFolderScreen
import com.pwhs.quickmem.presentation.app.search_result.study_set.ListResultStudySetScreen
import com.pwhs.quickmem.presentation.app.search_result.study_set.component.FilterStudySetBottomSheet
import com.pwhs.quickmem.presentation.app.search_result.study_set.enums.SearchResultSizeEnum
import com.pwhs.quickmem.presentation.app.search_result.user.ListResultUserScreen
import com.pwhs.quickmem.presentation.components.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.FolderDetailScreenDestination
import com.ramcosta.composedestinations.generated.destinations.StudySetDetailScreenDestination
import com.ramcosta.composedestinations.generated.destinations.UserDetailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination<RootGraph>(
    navArgs = SearchResultArgs::class
)
@Composable
fun SearchResultScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchResultViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    resultBackNavigator: ResultBackNavigator<Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }

    val studySetItems: LazyPagingItems<GetStudySetResponseModel> =
        viewModel.studySetState.collectAsLazyPagingItems()

    val folderItems: LazyPagingItems<GetFolderResponseModel> =
        viewModel.folderState.collectAsLazyPagingItems()


    val userItems: LazyPagingItems<SearchUserResponseModel> =
        viewModel.userState.collectAsLazyPagingItems()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SearchResultUiEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    SearchResult(
        modifier = modifier,
        tabIndex = tabIndex,
        query = uiState.query,
        onTabSelected = { tabIndex = it },
        studySets = studySetItems,
        folders = folderItems,
        users = userItems,
        colorModel = uiState.colorModel,
        onColorChange = {
            viewModel.onEvent(SearchResultUiAction.ColorChanged(it))
        },
        subjectModel = uiState.subjectModel,
        onSubjectChange = {
            viewModel.onEvent(SearchResultUiAction.SubjectChanged(it))
        },
        sizeModel = uiState.sizeStudySetModel,
        onSizeChange = {
            viewModel.onEvent(SearchResultUiAction.SizeChanged(it))
        },
        isAiGenerated = uiState.isAIGenerated,
        onIsAiGeneratedChange = {
            viewModel.onEvent(SearchResultUiAction.IsAiGeneratedChanged(it))
        },
        onApplyClick = {
            viewModel.onEvent(SearchResultUiAction.ApplyFilter)
        },
        onStudySetRefresh = {
            viewModel.onEvent(SearchResultUiAction.RefreshStudySets)
        },
        onFolderRefresh = {
            viewModel.onEvent(SearchResultUiAction.RefreshFolders)
        },
        onStudySetClick = {
            navigator.navigate(
                StudySetDetailScreenDestination(
                    id = it?.id ?: "",
                )
            )
        },
        onFolderClick = {
            navigator.navigate(
                FolderDetailScreenDestination(
                    id = it?.id ?: "",
                )
            )
        },
        onUserItemClicked = {
            navigator.navigate(
                UserDetailScreenDestination(
                    userId = it?.id ?: ""
                )
            )
        },
        onNavigateBack = {
            resultBackNavigator.navigateBack(true)
        },
        onResetClick = {
            viewModel.onEvent(SearchResultUiAction.ResetFilter)
        },
        onSeeAllClickFolder = { SearchResultEnum.FOLDER.index },
        onSeeAllClickStudySet = { SearchResultEnum.STUDY_SET.index },
        onSeeAllClickUsers = { SearchResultEnum.USER.index }
    )
}

@Composable
fun SearchResult(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    query: String = "",
    tabIndex: Int = SearchResultEnum.ALL_RESULT.index,
    onTabSelected: (Int) -> Unit = {},
    colorModel: ColorModel? = ColorModel.defaultColors.first(),
    onColorChange: (ColorModel) -> Unit = {},
    subjectModel: SubjectModel? = SubjectModel.defaultSubjects.first(),
    onSubjectChange: (SubjectModel) -> Unit = {},
    sizeModel: SearchResultSizeEnum = SearchResultSizeEnum.ALL,
    onSizeChange: (SearchResultSizeEnum) -> Unit = {},
    isAiGenerated: Boolean = false,
    onIsAiGeneratedChange: (Boolean) -> Unit = {},
    onApplyClick: () -> Unit = {},
    onStudySetRefresh: () -> Unit = {},
    onFolderRefresh: () -> Unit = {},
    studySets: LazyPagingItems<GetStudySetResponseModel>? = null,
    folders: LazyPagingItems<GetFolderResponseModel>? = null,
    users: LazyPagingItems<SearchUserResponseModel>? = null,
    onStudySetClick: (GetStudySetResponseModel?) -> Unit = {},
    onFolderClick: (GetFolderResponseModel?) -> Unit = {},
    onUserItemClicked: (SearchUserResponseModel?) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onResetClick: () -> Unit = {},
    onSeeAllClickStudySet: () -> Unit = {},
    onSeeAllClickFolder: () -> Unit = {},
    onSeeAllClickUsers: () -> Unit = {},
) {
    var showFilterBottomSheet by remember { mutableStateOf(false) }
    val tabTitles = listOf(
        stringResource(R.string.txt_all_result),
        stringResource(R.string.txt_study_set),
        stringResource(R.string.txt_folder),
        stringResource(R.string.txt_user)
    )
    Scaffold(
        containerColor = colorScheme.background,
        modifier = modifier,
        topBar = {
            TopBarSearchResult(
                onNavigateBack = onNavigateBack,
                title = stringResource(R.string.txt_result, query),
                onClickFilter = {
                    if (tabIndex == SearchResultEnum.STUDY_SET.index) {
                        showFilterBottomSheet = true
                    }
                },
                showFilterIcon = tabIndex == SearchResultEnum.STUDY_SET.index
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                SecondaryScrollableTabRow(
                    selectedTabIndex = tabIndex,
                    indicator = {
                        SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabIndex)
                        )
                    },
                    contentColor = colorScheme.onSurface,
                    edgePadding = 8.dp
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
                            onClick = { onTabSelected(index) },
                        )
                    }
                }
                when (tabIndex) {
                    SearchResultEnum.ALL_RESULT.index -> ListAllResultScreen(
                        studySets = studySets,
                        folders = folders,
                        users = users,
                        onStudySetClick = onStudySetClick,
                        onFolderClick = onFolderClick,
                        onUserItemClicked = onUserItemClicked,
                        onSeeAllClickFolder = onSeeAllClickFolder,
                        onSeeAllClickStudySet = onSeeAllClickStudySet,
                        onSeeAllClickUsers = onSeeAllClickUsers,
                    )

                    SearchResultEnum.STUDY_SET.index -> ListResultStudySetScreen(
                        studySets = studySets,
                        onStudySetClick = onStudySetClick,
                        onStudySetRefresh = onStudySetRefresh,
                    )

                    SearchResultEnum.FOLDER.index -> ListResultFolderScreen(
                        modifier = modifier,
                        folders = folders,
                        onFolderClick = onFolderClick,
                        onFolderRefresh = onFolderRefresh
                    )

                    SearchResultEnum.USER.index -> ListResultUserScreen(
                        modifier = modifier,
                        users = users,
                        onUserItemClicked = onUserItemClicked
                    )
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
    if (showFilterBottomSheet) {
        when (tabIndex) {
            SearchResultEnum.STUDY_SET.index -> FilterStudySetBottomSheet(
                colorModel = colorModel,
                onColorChange = onColorChange,
                onSubjectChange = onSubjectChange,
                subjectModel = subjectModel,
                onResetClick = onResetClick,
                onNavigateBack = {
                    showFilterBottomSheet = false
                },
                sizeModel = sizeModel,
                onSizeChange = onSizeChange,
                onIsAiGeneratedChange = onIsAiGeneratedChange,
                isAiGenerated = isAiGenerated,
                onApplyClick = {
                    onApplyClick()
                    showFilterBottomSheet = false
                }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
private fun SearchResultScreen() {
    QuickMemTheme {
        SearchResult()
    }
}
