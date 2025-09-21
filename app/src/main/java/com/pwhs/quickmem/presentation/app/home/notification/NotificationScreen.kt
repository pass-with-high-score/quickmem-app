package com.pwhs.quickmem.presentation.app.home.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.notification.GetNotificationResponseModel
import com.pwhs.quickmem.presentation.app.home.components.NotificationItem
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination<RootGraph>
@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()

    Notification(
        isLoading = uiState.isLoading,
        notifications = uiState.notifications,
        onMarkAsRead = { notificationId ->
            viewModel.onEvent(NotificationUiAction.MarkAsRead(notificationId))
        },
        onClearAll = {
            viewModel.onEvent(NotificationUiAction.ClearAllNotifications)
        },
        onNavigateBack = {
            resultNavigator.navigateBack(true)
        },
        onRefresh = {
            viewModel.onEvent(NotificationUiAction.RefreshNotifications)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notification(
    isLoading: Boolean = false,
    notifications: List<GetNotificationResponseModel> = emptyList(),
    onMarkAsRead: (String) -> Unit = {},
    onClearAll: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    val refreshState = rememberPullToRefreshState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.txt_notifications),
                        style = typography.titleMedium
                    )
                },
                actions = {
                    if (notifications.isNotEmpty()) {
                        Button(
                            onClick = onClearAll,
                        ) {
                            Text(
                                text = stringResource(R.string.txt_clear_all),
                                style = typography.bodyMedium
                            )
                        }
                    }
                }

            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            state = refreshState,
            onRefresh = onRefresh,
            isRefreshing = isLoading,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            when {
                notifications.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsOff,
                            contentDescription = null,
                            modifier = Modifier.size(50.dp),
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(R.string.txt_there_are_no_notifications),
                            style = typography.bodyMedium.copy(
                                color = colorScheme.onSurface,
                            ),
                        )
                    }
                }

                else -> {
                    LazyColumn {
                        items(items = notifications, key = { it.id }) { notification ->
                            NotificationItem(
                                notification = notification,
                                onMarkAsRead = {
                                    onMarkAsRead(it)
                                },
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 10.dp),
                                thickness = 1.dp,
                                color = colorScheme.onSurface.copy(alpha = 0.2f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
private fun NotificationScreenPreview() {
    QuickMemTheme {
        Notification()
    }
}