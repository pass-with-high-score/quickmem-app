package com.pwhs.quickmem.presentation.app.user_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val appManager: AppManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UserDetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            val userId: String = savedStateHandle.get<String>("userId") ?: ""
            val localUserId: String = appManager.userId.firstOrNull() ?: ""
            val isOwner: Boolean = userId == localUserId
            _uiState.update {
                it.copy(
                    userId = userId,
                    isOwner = isOwner
                )
            }
            loadUserDetails()
        }
    }

    fun onEvent(event: UserDetailUiAction) {
        when (event) {
            is UserDetailUiAction.Refresh -> {
                loadUserDetails()
            }
        }
    }

    private fun loadUserDetails() {
        viewModelScope.launch {
            val userId: String = uiState.value.userId
            authRepository.getUserDetail(userId = userId)
                .collect { resource ->
                    when (resource) {
                        is Resources.Loading -> {
                            _uiState.value =
                                _uiState.value.copy(isLoading = true, errorMessage = null)
                        }

                        is Resources.Success -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                role = resource.data?.role ?: "",
                                userName = resource.data?.username ?: "",
                                avatarUrl = resource.data?.avatarUrl ?: "",
                                studySets = resource.data?.studySets ?: emptyList(),
                                folders = resource.data?.folders ?: emptyList()
                            )
                        }

                        is Resources.Error -> {
                            _uiState.value = _uiState.value.copy(isLoading = false)
                            _uiEvent.send(UserDetailUiEvent.ShowError(resource.message ?: ""))
                        }
                    }
                }
        }
    }
}
