package com.pwhs.quickmem.presentation.app.home

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.classes.GetClassByOwnerResponseModel
import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.domain.model.subject.SubjectModel
import com.pwhs.quickmem.presentation.app.home.components.ClassHomeItem
import com.pwhs.quickmem.presentation.app.home.components.FolderHomeItem
import com.pwhs.quickmem.presentation.app.home.components.StreakCalendar
import com.pwhs.quickmem.presentation.app.home.components.StudySetHomeItem
import com.pwhs.quickmem.presentation.app.home.components.SubjectItem
import com.pwhs.quickmem.presentation.app.paywall.Paywall
import com.pwhs.quickmem.presentation.components.ActionButtonTopAppBar
import com.pwhs.quickmem.presentation.components.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.ui.theme.firasansExtraboldFont
import com.pwhs.quickmem.ui.theme.premiumColor
import com.pwhs.quickmem.ui.theme.streakTextColor
import com.pwhs.quickmem.ui.theme.streakTitleColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.annotation.parameters.DeepLink
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.ClassDetailScreenDestination
import com.ramcosta.composedestinations.generated.destinations.CreateStudySetScreenDestination
import com.ramcosta.composedestinations.generated.destinations.FolderDetailScreenDestination
import com.ramcosta.composedestinations.generated.destinations.JoinClassScreenDestination
import com.ramcosta.composedestinations.generated.destinations.NotificationScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SearchScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SearchStudySetBySubjectScreenDestination
import com.ramcosta.composedestinations.generated.destinations.StudySetDetailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import com.revenuecat.purchases.CustomerInfo
import java.time.LocalDate

@OptIn(ExperimentalPermissionsApi::class)
@Destination<RootGraph>(
    deepLinks = [
        DeepLink(uriPattern = "quickmem://join/class?code={classCode}"),
        DeepLink(uriPattern = "quickmem://share/folder?code={folderCode}"),
        DeepLink(uriPattern = "quickmem://share/study-set?code={studySetCode}")
    ]
)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    studySetCode: String? = null,
    folderCode: String? = null,
    classCode: String? = null,
    viewModel: HomeViewModel = hiltViewModel(),
    resultStudySetDetail: ResultRecipient<StudySetDetailScreenDestination, Boolean>,
    resultClassDetail: ResultRecipient<ClassDetailScreenDestination, Boolean>,
    resultFolderDetail: ResultRecipient<FolderDetailScreenDestination, Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var ssCode by rememberSaveable { mutableStateOf(studySetCode) }
    var fCode by rememberSaveable { mutableStateOf(folderCode) }
    var cCode by rememberSaveable { mutableStateOf(classCode) }
    LaunchedEffect(key1 = ssCode, key2 = fCode, key3 = cCode) {
        when {
            cCode != null -> {
                cCode = null
                navigator.navigate(
                    JoinClassScreenDestination(
                        code = classCode ?: ""
                    )
                )
            }

            fCode != null -> {
                fCode = null
                navigator.navigate(
                    FolderDetailScreenDestination(
                        code = folderCode ?: ""
                    )
                )
            }

            ssCode != null -> {
                ssCode = null
                navigator.navigate(
                    StudySetDetailScreenDestination(
                        code = studySetCode
                    )
                )
            }
        }
    }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                HomeUiEvent.UnAuthorized -> {
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
    resultStudySetDetail.onNavResult { result ->
        when (result) {
            NavResult.Canceled -> {
            }

            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(HomeUiAction.RefreshHome)
                }
            }
        }
    }

    resultClassDetail.onNavResult { result ->
        when (result) {
            NavResult.Canceled -> {
            }

            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(HomeUiAction.RefreshHome)
                }
            }
        }
    }

    resultFolderDetail.onNavResult { result ->
        when (result) {
            NavResult.Canceled -> {
            }

            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(HomeUiAction.RefreshHome)
                }
            }
        }
    }
    val notificationPermission =
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    LaunchedEffect(notificationPermission) {
        if (!notificationPermission.status.isGranted) {
            notificationPermission.launchPermissionRequest()
            viewModel.onEvent(HomeUiAction.OnChangeAppPushNotifications(false))
        } else {
            viewModel.onEvent(HomeUiAction.OnChangeAppPushNotifications(true))
        }
    }
    Home(
        isLoading = uiState.isLoading,
        modifier = modifier,
        subjects = uiState.subjects,
        studySets = uiState.studySets,
        folders = uiState.folders,
        classes = uiState.classes,
        notificationCount = uiState.notificationCount,
        onStudySetClick = {
            navigator.navigate(
                StudySetDetailScreenDestination(
                    id = it.id,
                )
            )
        },
        onClassClicked = {
            navigator.navigate(
                ClassDetailScreenDestination(
                    id = it.id,
                    title = it.title,
                    description = it.description
                )
            )
        },
        onFolderClick = {
            navigator.navigate(
                FolderDetailScreenDestination(
                    id = it.id,
                )
            )
        },
        onNavigateToSearch = {
            navigator.navigate(SearchScreenDestination)
        },
        customer = uiState.customerInfo,
        onCustomerInfoChanged = { customerInfo ->
            viewModel.onEvent(HomeUiAction.OnChangeCustomerInfo(customerInfo))
        },
        onSearchStudySetBySubject = { subject ->
            navigator.navigate(
                SearchStudySetBySubjectScreenDestination(
                    id = subject.id,
                    studySetCount = subject.studySetCount,
                    icon = subject.iconRes ?: R.drawable.ic_all
                )
            )
        },
        onClickToCreateStudySet = {
            navigator.navigate(CreateStudySetScreenDestination())
        },
        onHomeRefresh = {
            viewModel.onEvent(HomeUiAction.RefreshHome)
        },
        streakCount = uiState.streakCount,
        streakDates = uiState.streakDates,
        onNavigateToNotification = {
            navigator.navigate(NotificationScreenDestination())
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
private fun Home(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onHomeRefresh: () -> Unit = {},
    subjects: List<SubjectModel> = emptyList(),
    studySets: List<GetStudySetResponseModel> = emptyList(),
    folders: List<GetFolderResponseModel> = emptyList(),
    classes: List<GetClassByOwnerResponseModel> = emptyList(),
    onClassClicked: (GetClassByOwnerResponseModel) -> Unit = {},
    onStudySetClick: (GetStudySetResponseModel) -> Unit = {},
    onFolderClick: (GetFolderResponseModel) -> Unit = {},
    notificationCount: Int = 0,
    onNavigateToSearch: () -> Unit = {},
    onClickToCreateStudySet: () -> Unit = {},
    customer: CustomerInfo? = null,
    onCustomerInfoChanged: (CustomerInfo) -> Unit = {},
    onSearchStudySetBySubject: (SubjectModel) -> Unit = {},
    streakCount: Int = 0,
    streakDates: List<LocalDate> = emptyList(),
    onNavigateToNotification: () -> Unit = {},
) {

    val streakBottomSheet = rememberModalBottomSheetState()
    var showStreakBottomSheet by remember {
        mutableStateOf(false)
    }

    var isPaywallVisible by remember {
        mutableStateOf(false)
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_fire_streak))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )

    val refreshState = rememberPullToRefreshState()

    Scaffold(
        modifier = modifier,
        topBar = {
            LargeTopAppBar(
                navigationIcon = {
                    Text(
                        when (customer?.activeSubscriptions?.isNotEmpty()) {
                            true -> stringResource(R.string.txt_quickmem_plus)
                            false -> stringResource(R.string.txt_quickmem)
                            null -> stringResource(R.string.txt_quickmem)
                        },
                        style = typography.titleLarge.copy(
                            fontFamily = firasansExtraboldFont,
                            color = when (customer?.activeSubscriptions?.isNotEmpty()) {
                                true -> premiumColor
                                false -> colorScheme.primary
                                null -> colorScheme.primary
                            }
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                },
                expandedHeight = 160.dp,
                collapsedHeight = 56.dp,
                title = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(end = 16.dp, bottom = 20.dp),
                        shape = CircleShape,
                        onClick = onNavigateToSearch,
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = stringResource(R.string.txt_search),
                                tint = colorScheme.secondary,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = stringResource(R.string.txt_study_sets_folders_class),
                                style = typography.bodyMedium.copy(
                                    color = colorScheme.secondary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                },
                actions = {
                    ActionButtonTopAppBar(
                        onClick = {
                            showStreakBottomSheet = true
                        },
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_fire),
                                contentDescription = stringResource(R.string.txt_streak),
                                modifier = Modifier
                                    .size(28.dp)
                            )
                            Text(
                                text = "$streakCount",
                                style = typography.titleLarge.copy(
                                    color = streakTitleColor,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .background(color = Color.White, shape = CircleShape)
                            .border(
                                width = 2.dp,
                                color = colorScheme.primary,
                                shape = CircleShape
                            )
                            .clickable {
                                onNavigateToNotification()
                            }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = stringResource(R.string.txt_notifications),
                            tint = colorScheme.primary,
                            modifier = Modifier
                                .size(30.dp)

                        )
                        if (notificationCount > 0) {
                            Badge(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(10.dp),
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            Spacer(modifier = Modifier.height(100.dp))
        },
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier.fillMaxWidth(),
            state = refreshState,
            isRefreshing = isLoading,
            onRefresh = {
                onHomeRefresh()
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                if (studySets.isEmpty() && classes.isEmpty() && folders.isEmpty() && !isLoading) {
                    item {
                        Text(
                            text = stringResource(R.string.txt_here_is_how_to_get_started),
                            style = typography.titleMedium.copy(
                                color = colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            onClick = onClickToCreateStudySet,
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_three_cards),
                                    contentDescription = stringResource(R.string.txt_create_a_flashcard),
                                    tint = Color.Blue,
                                    modifier = Modifier
                                        .size(50.dp)
                                )

                                Text(
                                    text = stringResource(R.string.txt_create_your_own_flashcards),
                                    style = typography.titleMedium.copy(
                                        color = colorScheme.onSurface,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .padding(vertical = 10.dp)
                                        .padding(start = 10.dp)
                                        .weight(1f)
                                )
                            }
                        }
                    }
                }
                if (studySets.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.txt_study_sets),
                            style = typography.titleMedium.copy(
                                color = colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }

                    item {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            items(items = studySets, key = { it.id }) { studySet ->
                                StudySetHomeItem(
                                    studySet = studySet,
                                    onStudySetClick = { onStudySetClick(studySet) }
                                )
                            }
                        }
                    }
                }

                if (folders.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.txt_folders),
                            style = typography.titleMedium.copy(
                                color = colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }

                    item {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            items(items = folders, key = { it.id }) { folder ->
                                FolderHomeItem(
                                    title = folder.title,
                                    numOfStudySets = folder.studySetCount,
                                    onClick = { onFolderClick(folder) },
                                    userResponseModel = folder.owner,
                                )
                            }
                        }
                    }
                }

                if (classes.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.txt_classes),
                            style = typography.titleMedium.copy(
                                color = colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }

                    item {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            items(items = classes, key = { it.id }) { classItem ->
                                ClassHomeItem(
                                    classItem = classItem,
                                    onClick = { onClassClicked(classItem) }
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = stringResource(R.string.txt_top_5_subjects_have_study_sets),
                        style = typography.titleMedium.copy(
                            color = colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
                items(items = subjects, key = { it.id }) { subject ->
                    SubjectItem(
                        subject = subject,
                        onSearchStudySetBySubject = {
                            onSearchStudySetBySubject(subject)
                        },
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
        Paywall(
            isPaywallVisible = isPaywallVisible,
            onCustomerInfoChanged = { customerInfo ->
                onCustomerInfoChanged(customerInfo)
            },
            onPaywallDismissed = {
                isPaywallVisible = false
            },
            userId = customer?.originalAppUserId ?: ""
        )

    }
    if (showStreakBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showStreakBottomSheet = false
            },
            sheetState = streakBottomSheet,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )
                Text(
                    text = "$streakCount",
                    style = typography.titleLarge.copy(
                        color = streakTitleColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 52.sp
                    )
                )
                Text(
                    text = when (streakCount) {
                        1 -> stringResource(R.string.txt_day_streak)
                        else -> stringResource(R.string.txt_days_streak)
                    },
                    style = typography.titleLarge.copy(
                        color = streakTextColor,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = stringResource(R.string.txt_practice_every_day),
                )
                StreakCalendar(
                    streakDates = streakDates
                )
            }
        }
    }
    LoadingOverlay(isLoading = isLoading)
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
private fun HomeScreenPreview() {
    QuickMemTheme {
        Home(
            subjects = listOf(
                SubjectModel(
                    id = 1,
                    subjectName = R.string.txt_general,
                    iconRes = R.drawable.ic_all,
                    color = Color(0xFF7f60f9),
                    studySetCount = 1
                ),
            )
        )
    }
}
