package com.pwhs.quickmem.presentation.auth.update_fullname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.auth.UpdateFullNameRequestModel
import com.pwhs.quickmem.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateFullNameViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val appManager: AppManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UpdateFullNameUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UpdateFullNameUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: UpdateFullNameUiAction) {
        when (event) {
            is UpdateFullNameUiAction.FullNameChanged -> {
                if (event.fullname.isBlank()) {
                    _uiState.update {
                        it.copy(
                            fullName = event.fullname,
                            errorMessage = R.string.txt_your_name_cannot_be_empty
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            fullName = event.fullname,
                            errorMessage = null
                        )
                    }
                }
            }

            is UpdateFullNameUiAction.Submit -> {
                updateFullName()
            }
        }
    }

    private fun updateFullName() {
        viewModelScope.launch {


            _uiState.update { it.copy(isLoading = true) }

            authRepository.updateFullName(
                UpdateFullNameRequestModel(
                    fullname = _uiState.value.fullName
                )
            ).collect { resource ->
                when (resource) {
                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = R.string.txt_error_occurred
                            )
                        }
                    }

                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                fullName = resource.data?.fullname ?: ""
                            )
                        }
                        appManager.saveUserFullName(_uiState.value.fullName)
                        _uiEvent.send(UpdateFullNameUiEvent.UpdateSuccess)
                    }
                }
            }
        }
    }
}