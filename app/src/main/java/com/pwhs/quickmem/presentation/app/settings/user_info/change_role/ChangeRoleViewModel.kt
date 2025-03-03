package com.pwhs.quickmem.presentation.app.settings.user_info.change_role

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.core.data.enums.UserRole
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.auth.ChangeRoleRequestModel
import com.pwhs.quickmem.domain.repository.AuthRepository
import com.pwhs.quickmem.utils.isDateSmallerThan
import com.pwhs.quickmem.utils.toTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ChangeRoleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val appManager: AppManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangeRoleUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<ChangeRoleUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val role = savedStateHandle.get<String>("role") ?: ""
        _uiState.update {
            it.copy(
                role = when (role) {
                    "STUDENT" -> UserRole.STUDENT
                    "TEACHER" -> UserRole.TEACHER
                    else -> UserRole.STUDENT
                }
            )
        }
    }

    fun onEvent(event: ChangeRoleUiAction) {
        when (event) {
            is ChangeRoleUiAction.SelectRole -> {
                _uiState.update { it.copy(role = event.role, errorMessage = null) }
            }

            is ChangeRoleUiAction.SaveRole -> {
                changeRole()
            }
        }
    }

    private fun changeRole() {
        viewModelScope.launch {
            val birthday = appManager.userBirthday.firstOrNull() ?: ""
            val dateLong = birthday.toTimestamp()
            val isUserUnderAge = dateLong?.isDateSmallerThan() == true
            if (isUserUnderAge) {
                _uiEvent.send(
                    ChangeRoleUiEvent.ShowUnderageDialog(
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                            Date(dateLong)
                        )
                    )
                )
                return@launch
            }
            val role = _uiState.value.role
            authRepository.changeRole(
                changeRoleRequestModel = ChangeRoleRequestModel(
                    role = role.name
                )
            ).collect { resource ->
                when (resource) {
                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message
                            )
                        }
                        _uiEvent.send(
                            ChangeRoleUiEvent.ShowError(
                                resource.message ?: "An error occurred"
                            )
                        )
                    }

                    is Resources.Success -> {
                        appManager.saveUserRole(role.name)
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEvent.send(ChangeRoleUiEvent.RoleChangedSuccessfully)
                    }
                }
            }
        }
    }
}
