package com.pwhs.quickmem.presentation.app.classes.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.classes.CreateClassRequestModel
import com.pwhs.quickmem.domain.repository.ClassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateClassViewModel @Inject constructor(
    private val classRepository: ClassRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateClassUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<CreateClassUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: CreateClassUiAction) {
        when (event) {
            is CreateClassUiAction.DescriptionChanged -> {
                _uiState.update {
                    it.copy(description = event.description)
                }
            }

            is CreateClassUiAction.MemberManagementChanged -> {
                _uiState.update {
                    it.copy(allowMemberManagement = event.allowMemberManagement)
                }
            }

            is CreateClassUiAction.TitleChanged -> {
                _uiState.update {
                    it.copy(title = event.title)
                }
            }

            CreateClassUiAction.SaveClicked -> {
                createNewClass()
            }

            is CreateClassUiAction.SetManagementChanged -> {
                _uiState.update {
                    it.copy(allowSetManagement = event.allowSetManagement)
                }
            }
        }
    }

    private fun createNewClass() {
        viewModelScope.launch {
            val title = _uiState.value.title
            val description = _uiState.value.description
            val allowMemberManagement = _uiState.value.allowMemberManagement
            val allowSetManagement = _uiState.value.allowSetManagement

            classRepository.createClass(
                createClassRequestModel = CreateClassRequestModel(
                    description = description,
                    title = title,
                    allowSetManagement = allowSetManagement,
                    allowMemberManagement = allowMemberManagement
                )
            ).collectLatest { resource ->
                when (resource) {
                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.send(CreateClassUiEvent.ShowError(resource.message!!))
                    }

                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.send(CreateClassUiEvent.ClassesCreated(resource.data!!.id))
                    }
                }
            }
        }
    }
}