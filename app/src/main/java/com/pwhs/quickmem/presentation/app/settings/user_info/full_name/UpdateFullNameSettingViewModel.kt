package com.pwhs.quickmem.presentation.app.settings.user_info.full_name

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.auth.UpdateFullNameRequestModel
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
class UpdateFullNameSettingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tokenManager: TokenManager,
    private val appManager: AppManager,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UpdateFullNameSettingUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UpdateFullNameSettingUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val fullName: String = savedStateHandle.get<String>("fullName") ?: ""
        _uiState.update {
            it.copy(
                fullName = fullName
            )
        }
    }

    fun onEvent(event: UpdateFullNameSettingUiAction) {
        when (event) {
            is UpdateFullNameSettingUiAction.OnFullNameChanged -> {
                _uiState.update {
                    it.copy(
                        fullName = event.fullName,
                        errorMessage = ""
                    )
                }
            }

            is UpdateFullNameSettingUiAction.OnSaveClicked -> {
                saveFullName()
            }
        }
    }

    private fun saveFullName() {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            val fullname = _uiState.value.fullName
            authRepository.updateFullName(
                token,
                UpdateFullNameRequestModel(fullname = fullname)
            )
                .collect { resource ->
                    when (resource) {
                        is Resources.Error -> {
                            _uiState.update {
                                it.copy(
                                    errorMessage = resource.message ?: "An error occurred",
                                    isLoading = false
                                )
                            }
                            _uiEvent.send(
                                UpdateFullNameSettingUiEvent.OnError(
                                    resource.message ?: "An error occurred"
                                )
                            )
                        }

                        is Resources.Loading -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = true
                                )
                            }
                        }

                        is Resources.Success -> {
                            appManager.saveUserFullName(resource.data?.fullname ?: fullname)
                            _uiEvent.send(UpdateFullNameSettingUiEvent.OnFullNameChanged)
                        }
                    }

                }
        }
    }
}