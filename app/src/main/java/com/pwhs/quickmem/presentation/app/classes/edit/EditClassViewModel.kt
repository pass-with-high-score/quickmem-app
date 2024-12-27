package com.pwhs.quickmem.presentation.app.classes.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.classes.UpdateClassRequestModel
import com.pwhs.quickmem.domain.repository.ClassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditClassViewModel @Inject constructor(
    private val classRepository: ClassRepository,
    private val tokenManager: TokenManager,
    private val appManager: AppManager,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditClassUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<EditClassUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val classId: String = savedStateHandle.get<String>("classId") ?: ""
        val classTitle: String = savedStateHandle.get<String>("classTitle") ?: ""
        val classDescription: String = savedStateHandle.get<String>("classDescription") ?: ""
        val allowSet: Boolean = savedStateHandle.get<Boolean>("isSetAllowed") == true
        val allowMember: Boolean = savedStateHandle.get<Boolean>("isMemberAllowed") == true

        _uiState.update {
            it.copy(
                id = classId,
                title = classTitle,
                description = classDescription,
                allowSetManagement = allowSet,
                allowMemberManagement = allowMember
            )
        }
    }

    fun onEvent(event: EditClassUiAction) {
        when (event) {
            is EditClassUiAction.DescriptionChanged -> {
                _uiState.update {
                    it.copy(description = event.description)
                }
            }

            is EditClassUiAction.OnAllowMemberChanged -> {
                _uiState.update {
                    it.copy(allowMemberManagement = event.allowMemberChanged)
                }
            }

            is EditClassUiAction.OnAllowSetChanged -> {
                _uiState.update {
                    it.copy(allowSetManagement = event.allowSetChanged)
                }
            }

            EditClassUiAction.SaveClicked -> {
                updateClass(_uiState.value.id)
            }

            is EditClassUiAction.TitleChanged -> {
                _uiState.update {
                    it.copy(title = event.title)
                }
            }
        }
    }

    private fun updateClass(id: String) {
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: ""
            val userId = appManager.userId.firstOrNull() ?: ""
            val uiState = _uiState.value
            classRepository.updateClass(
                token = token,
                classId = id,
                updateClassRequestModel = UpdateClassRequestModel(
                    ownerId = userId,
                    allowMemberManagement = uiState.allowMemberManagement,
                    allowSetManagement = uiState.allowSetManagement,
                    description = uiState.description,
                    title = uiState.title
                )
            ).collectLatest { resources ->
                when (resources) {
                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.send(EditClassUiEvent.ShowError)
                    }

                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.send(EditClassUiEvent.ClassesUpdated)
                    }
                }
            }
        }
    }
}