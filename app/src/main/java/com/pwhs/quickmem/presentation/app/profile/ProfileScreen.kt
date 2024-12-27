package com.pwhs.quickmem.presentation.app.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.outlinedButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
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
import com.pwhs.quickmem.data.mapper.study_time.toStudyTimeModel
import com.pwhs.quickmem.domain.model.study_time.GetStudyTimeByUserResponseModel
import com.pwhs.quickmem.presentation.app.paywall.Paywall
import com.pwhs.quickmem.presentation.component.LearningTimeBars
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.ui.theme.firasansExtraboldFont
import com.pwhs.quickmem.ui.theme.premiumColor
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
        name = uiState.username,
        role = uiState.role,
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
        studyTime = uiState.studyTime
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    modifier: Modifier = Modifier,
    name: String = "",
    role: String = "",
    onRefresh: () -> Unit = {},
    isLoading: Boolean = false,
    avatarUrl: String = "",
    onAvatarClick: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    onCustomerInfoChanged: (customerInfo: CustomerInfo) -> Unit = {},
    customerInfo: CustomerInfo? = null,
    studyTime: GetStudyTimeByUserResponseModel? = null,
) {
    var isPaywallVisible by remember {
        mutableStateOf(false)
    }

    val refreshState = rememberPullToRefreshState()
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Text(
                        when (customerInfo?.activeSubscriptions?.isNotEmpty()) {
                            true -> stringResource(R.string.txt_quickmem_plus)
                            false -> stringResource(R.string.txt_quickmem)
                            null -> stringResource(R.string.txt_quickmem)
                        },
                        style = typography.titleLarge.copy(
                            fontFamily = firasansExtraboldFont,
                            color = when (customerInfo?.activeSubscriptions?.isNotEmpty()) {
                                true -> premiumColor
                                false -> colorScheme.primary
                                null -> colorScheme.primary
                            }
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                },
                title = {},
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
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(avatarUrl.ifEmpty { null })
                            .build(),
                        contentDescription = stringResource(R.string.txt_user_avatar),
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .size(80.dp)
                            .clip(CircleShape)
                            .clickable { onAvatarClick() },
                        contentScale = ContentScale.Crop
                    )
                }

                item {
                    Text(
                        text = name,
                        style = typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    if (role == "TEACHER") {
                        Text(
                            text = stringResource(R.string.txt_teacher),
                            style = typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = colorScheme.secondary,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
                item {
                    OutlinedButton(
                        onClick = navigateToSettings,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        shape = shapes.large,
                        border = BorderStroke(
                            width = 1.dp,
                            color = colorScheme.onSurface
                        ),
                        colors = outlinedButtonColors(
                            contentColor = colorScheme.onSurface
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = stringResource(R.string.txt_settings),
                                    modifier = Modifier.size(30.dp)
                                )
                                Text(
                                    text = stringResource(R.string.txt_your_settings),
                                    style = typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Navigate to settings",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
                item {
                    AnimatedVisibility(
                        visible = studyTime?.flip != 0 || studyTime.quiz != 0 || studyTime.total != 0 || studyTime.write != 0,
                    ) {
                        LearningTimeBars(
                            studyTime = studyTime?.toStudyTimeModel(),
                            color = colorScheme.primary,
                            modifier = Modifier
                                .height(310.dp)
                        )
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
@Composable
fun ProfilePreview() {
    QuickMemTheme {
        Profile(
            name = "John Doe",
            role = "TEACHER",
            avatarUrl = "https://www.example.com/avatar.jpg"
        )
    }
}







