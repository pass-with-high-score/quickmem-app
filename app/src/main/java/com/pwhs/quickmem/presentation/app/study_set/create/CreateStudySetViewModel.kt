package com.pwhs.quickmem.presentation.app.study_set.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.study_set.CreateStudySetRequestModel
import com.pwhs.quickmem.domain.model.subject.SubjectModel
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateStudySetViewModel @Inject constructor(
    private val studySetRepository: StudySetRepository,
    private val appManager: AppManager,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateStudySetUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<CreateStudySetUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        try {
            val subjectId: Int? = savedStateHandle.get<Int>("subjectId")
            if (subjectId != null) {
                _uiState.update {
                    it.copy(subjectModel = SubjectModel.defaultSubjects.first { it.id == subjectId })
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun onEvent(event: CreateStudySetUiAction) {
        when (event) {
            is CreateStudySetUiAction.ColorChanged -> {
                _uiState.update {
                    it.copy(colorModel = event.colorModel)
                }
            }

            is CreateStudySetUiAction.PublicChanged -> {
                _uiState.update {
                    it.copy(isPublic = event.isPublic)
                }
            }

            is CreateStudySetUiAction.SaveClicked -> {
                val uiState = _uiState.value
                if (uiState.title.isEmpty()) {
                    _uiState.update {
                        it.copy(titleError = R.string.txt_title_is_required)
                    }
                    return
                } else {
                    _uiState.update {
                        it.copy(titleError = null)
                    }
                    createStudySet()
                }
            }

            is CreateStudySetUiAction.SubjectChanged -> {
                _uiState.update {
                    it.copy(subjectModel = event.subjectModel)
                }
            }

            is CreateStudySetUiAction.TitleChanged -> {
                _uiState.update {
                    it.copy(title = event.title)
                }
            }

            is CreateStudySetUiAction.DescriptionChanged -> {
                _uiState.update {
                    it.copy(description = event.description)
                }
            }
        }
    }

    private fun createStudySet() {
        viewModelScope.launch {
            val ownerId = appManager.userId.firstOrNull { true } ?: ""
            val createStudySetRequestModel = CreateStudySetRequestModel(
                title = _uiState.value.title,
                subjectId = _uiState.value.subjectModel.id,
                colorId = _uiState.value.colorModel.id,
                isPublic = _uiState.value.isPublic,
                description = _uiState.value.description,
                ownerId = ownerId
            )
            studySetRepository.createStudySet(
                createStudySetRequestModel = createStudySetRequestModel
            ).collectLatest { resource ->
                when (resource) {
                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.send(CreateStudySetUiEvent.StudySetCreated(resource.data!!.id))
                    }

                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.send(CreateStudySetUiEvent.ShowError(resource.message!!))
                    }
                }
            }
        }
    }
}