package com.pwhs.quickmem.presentation.app.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.study_time.GetStudyTimeByUserResponseModel
import com.pwhs.quickmem.presentation.app.paywall.Paywall
import com.pwhs.quickmem.presentation.app.profile.components.StatisticsCard
import com.pwhs.quickmem.presentation.components.ActionButtonTopAppBar
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.ui.theme.premiumColor
import com.pwhs.quickmem.utils.formatDate
import com.pwhs.quickmem.utils.toTimeString
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ChangeAvatarScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import com.revenuecat.purchases.CustomerInfo

@Composable
@Destination<RootGraph>
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    resultBackNavigator: ResultRecipient<ChangeAvatarScreenDestination, Boolean>,
) {

    val uiState by viewModel.uiState.collectAsState()

    resultBackNavigator.onNavResult { result ->
        when (result) {
            NavResult.Canceled -> {
                // Do nothing
            }

            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(ProfileUiAction.Refresh)
                }
            }
        }
    }

    Profile(
        modifier = modifier,
        fullName = uiState.fullName,
        username = uiState.username,
        avatarUrl = uiState.userAvatar,
        isLoading = uiState.isLoading,
        onRefresh = {
            viewModel.onEvent(ProfileUiAction.Refresh)
        },
        onAvatarClick = {
            navigator.navigate(ChangeAvatarScreenDestination)
        },
        navigateToSettings = {
            navigator.navigate(SettingsScreenDestination)
        },
        onCustomerInfoChanged = { customerInfo ->
            viewModel.onEvent(ProfileUiAction.OnChangeCustomerInfo(customerInfo))
        },
        customerInfo = uiState.customerInfo,
        studyTime = uiState.studyTime,
        createdAt = uiState.createDate,
        coins = uiState.coins,
        studySetCount = uiState.studySetCount,
        folderCount = uiState.folderCount,
        streakCount = uiState.streakCount,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    modifier: Modifier = Modifier,
    fullName: String = "",
    username: String = "",
    onRefresh: () -> Unit = {},
    isLoading: Boolean = false,
    avatarUrl: String = "",
    onAvatarClick: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    onCustomerInfoChanged: (customerInfo: CustomerInfo) -> Unit = {},
    customerInfo: CustomerInfo? = null,
    studyTime: GetStudyTimeByUserResponseModel? = null,
    createdAt: String? = null,
    coins: Int = 0,
    studySetCount: Int = 0,
    folderCount: Int = 0,
    streakCount: Int = 0,
) {
    var isPaywallVisible by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val refreshState = rememberPullToRefreshState()
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.txt_profile),
                        style = typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                actions = {
                    if (customerInfo?.activeSubscriptions?.isEmpty() == true) {
                        Button(
                            onClick = {
                                isPaywallVisible = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = premiumColor
                            ),
                            modifier = Modifier.padding(end = 8.dp),
                            shape = shapes.extraLarge,
                        ) {
                            Text(
                                text = stringResource(R.string.txt_upgrade),
                                style = typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                    ActionButtonTopAppBar(
                        onClick = navigateToSettings
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = stringResource(R.string.txt_notifications),
                            tint = colorScheme.primary,
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            isRefreshing = isLoading,
            state = refreshState,
            onRefresh = onRefresh
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(avatarUrl.ifEmpty { null })
                                .placeholder(R.drawable.default_avatar)
                                .build(),
                            contentDescription = stringResource(R.string.txt_user_avatar),
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .size(80.dp)
                                .clip(CircleShape)
                                .clickable { onAvatarClick() },
                            contentScale = ContentScale.Crop
                        )
                        if (fullName.isNotEmpty()) {
                            Text(
                                text = fullName,
                                style = typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Text(
                            text = "@$username",
                            style = typography.titleMedium.copy(
                                color = colorScheme.secondary
                            )
                        )

                        if (createdAt != null) {
                            Text(
                                text = stringResource(
                                    R.string.txt_account_created_at,
                                    createdAt.formatDate()
                                ),
                                style = typography.bodyMedium.copy(
                                    color = colorScheme.secondary
                                )
                            )
                        }
                    }
                }
                item {
                    Column {
                        Text(
                            text = stringResource(R.string.txt_statistics),
                            style = typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            StatisticsCard(
                                modifier = Modifier.weight(1f),
                                value = streakCount.toString(),
                                icon = R.drawable.ic_streak_fire,
                                title = R.string.txt_streak
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            StatisticsCard(
                                modifier = Modifier.weight(1f),
                                value = coins.toString(),
                                icon = R.drawable.ic_coin,
                                title = R.string.txt_coin
                            )
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            StatisticsCard(
                                modifier = Modifier.weight(1f),
                                value = studySetCount.toString(),
                                icon = R.drawable.ic_study_set,
                                title = R.string.txt_study_set
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            StatisticsCard(
                                modifier = Modifier.weight(1f),
                                value = folderCount.toString(),
                                icon = R.drawable.ic_folder,
                                title = R.string.txt_folder
                            )
                        }
                    }
                }
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.txt_study_time),
                            style = typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        StatisticsCard(
                            modifier = Modifier.fillMaxWidth(),
                            value = studyTime?.total?.toTimeString(context)
                                ?: stringResource(R.string.txt_no_data),
                            icon = R.drawable.ic_clock,
                            title = R.string.txt_total_time
                        )

                        Spacer(modifier = Modifier.padding(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            StatisticsCard(
                                modifier = Modifier.weight(1f),
                                value = studyTime?.flip?.toTimeString(context)
                                    ?: stringResource(R.string.txt_no_data),
                                icon = R.drawable.ic_flipcard,
                                title = R.string.txt_flip_card
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            StatisticsCard(
                                modifier = Modifier.weight(1f),
                                value = studyTime?.quiz?.toTimeString(context)
                                    ?: stringResource(R.string.txt_no_data),
                                icon = R.drawable.ic_quiz,
                                title = R.string.txt_quiz
                            )
                        }

                        Spacer(modifier = Modifier.padding(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            StatisticsCard(
                                modifier = Modifier.weight(1f),
                                value = studyTime?.trueFalse?.toTimeString(context)
                                    ?: stringResource(R.string.txt_no_data),
                                icon = R.drawable.ic_tf,
                                title = R.string.txt_true_false
                            )

                            Spacer(modifier = Modifier.padding(8.dp))

                            StatisticsCard(
                                modifier = Modifier.weight(1f),
                                value = studyTime?.write?.toTimeString(context)
                                    ?: stringResource(R.string.txt_no_data),
                                icon = R.drawable.ic_write,
                                title = R.string.txt_write
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(bottom = 100.dp))
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
            )
        }
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
fun ProfilePreview() {
    QuickMemTheme {
        Profile(
            fullName = "Nguyen Quang Minh",
            username = "nqmgaming",
            avatarUrl = "https://www.example.com/avatar.jpg"
        )
    }
}







