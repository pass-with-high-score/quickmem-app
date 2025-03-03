package com.pwhs.quickmem.presentation.app.home.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<NotificationUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadNotifications()
    }

    fun onEvent(event: NotificationUiAction) {
        when (event) {
            is NotificationUiAction.ClearAllNotifications -> {
                clearAllNotifications()
            }

            is NotificationUiAction.MarkAsRead -> {
                markNotificationAsRead(event.notificationId)
            }

            is NotificationUiAction.RefreshNotifications -> {
                loadNotifications()
            }
        }
    }

    private fun markNotificationAsRead(notificationId: String) {
        viewModelScope.launch {
            notificationRepository.markNotificationAsRead(notificationId = notificationId)
                .collect { result ->
                    when (result) {
                        is Resources.Success -> _uiState.update { state ->
                            state.copy(
                                notifications = state.notifications.map { notification ->
                                    if (notification.id == notificationId) notification.copy(isRead = true) else notification
                                },
                                isLoading = false
                            )
                        }

                        is Resources.Error -> {
                            _uiState.update {
                                it.copy(
                                    errorMessage = R.string.txt_failed_to_mark_notification_as_read,
                                    isLoading = false
                                )
                            }
                        }

                        is Resources.Loading -> {
                            _uiState.update {
                                it.copy(isLoading = true)
                            }
                        }
                    }
                }
        }
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            notificationRepository.loadNotifications().collect { result ->
                when (result) {
                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resources.Success -> _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            notifications = result.data ?: emptyList(),
                            errorMessage = null
                        )
                    }

                    is Resources.Error -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = R.string.txt_failed_to_load_notifications
                        )
                    }
                }
            }
        }
    }

    private fun clearAllNotifications() {
        viewModelScope.launch {
            notificationRepository.clearAllNotifications().collect { result ->
                when (result) {
                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(
                                notifications = emptyList(),
                                isLoading = false
                            )
                        }
                    }

                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = R.string.txt_failed_to_clear_all_notifications,
                                isLoading = false
                            )
                        }
                    }

                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                }
            }
        }
    }
}