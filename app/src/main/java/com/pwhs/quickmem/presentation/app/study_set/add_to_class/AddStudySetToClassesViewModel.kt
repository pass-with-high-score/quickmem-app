package com.pwhs.quickmem.presentation.app.study_set.add_to_class

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.classes.AddStudySetToClassesRequestModel
import com.pwhs.quickmem.domain.repository.ClassRepository
import com.pwhs.quickmem.domain.repository.StudySetRepository
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
class AddStudySetToClassesViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val appManager: AppManager,
    private val classRepository: ClassRepository,
    private val studySetRepository: StudySetRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddStudySetToClassesUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<AddStudySetToClassesUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val studySetId: String = savedStateHandle.get<String>("studySetId") ?: ""
        _uiState.update { it.copy(studySetId = studySetId) }
        viewModelScope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: return@launch
            val ownerId = appManager.userId.firstOrNull() ?: return@launch
            val userAvatar = appManager.userAvatarUrl.firstOrNull() ?: return@launch
            val username = appManager.username.firstOrNull() ?: return@launch
            _uiState.update {
                it.copy(
                    token = token,
                    userId = ownerId,
                    userAvatar = userAvatar,
                    username = username
                )
            }
            getClasses()
        }
    }

    fun onEvent(event: AddStudySetToClassesUiAction) {
        when (event) {
            is AddStudySetToClassesUiAction.AddStudySetToClasses -> {
                doneClick()
            }

            is AddStudySetToClassesUiAction.ToggleStudySetImport -> {
                toggleClassesImport(event.classId)
            }

            is AddStudySetToClassesUiAction.RefreshClasses -> {
                getClasses()
            }
        }
    }

    private fun getClasses() {
        viewModelScope.launch {
            classRepository.getClassByOwnerId(
                folderId = null,
                studySetId = _uiState.value.studySetId
            ).collectLatest { resources ->
                when (resources) {
                    is Resources.Success -> {
                        _uiState.update { foldersUiState ->
                            foldersUiState.copy(
                                isLoading = false,
                                classes = resources.data ?: emptyList(),
                                classImportedIds = resources.data?.filter { it.isImported == true }
                                    ?.map { it.id } ?: emptyList()
                            )
                        }
                    }

                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.send(
                            AddStudySetToClassesUiEvent.Error(
                                R.string.txt_error_occurred
                            )
                        )
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

    private fun doneClick() {
        viewModelScope.launch {
            val addStudySetToClassesRequestModel = AddStudySetToClassesRequestModel(
                studySetId = _uiState.value.studySetId,
                classIds = _uiState.value.classImportedIds
            )
            studySetRepository.addStudySetToClasses(
                addStudySetToClassesRequestModel = addStudySetToClassesRequestModel
            ).collectLatest { resources ->
                when (resources) {
                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.send(AddStudySetToClassesUiEvent.StudySetAddedToClasses)
                    }

                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.send(
                            AddStudySetToClassesUiEvent.Error(
                                R.string.txt_error_occurred
                            )
                        )
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

    private fun toggleClassesImport(classId: String) {
        val classImportedIds = _uiState.value.classImportedIds.toMutableList()
        if (classImportedIds.contains(classId)) {
            classImportedIds.remove(classId)
        } else {
            classImportedIds.add(classId)
        }
        _uiState.update {
            it.copy(classImportedIds = classImportedIds)
        }
    }
}
