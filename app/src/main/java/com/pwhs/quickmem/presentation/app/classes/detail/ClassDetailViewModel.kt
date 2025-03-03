package com.pwhs.quickmem.presentation.app.classes.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.classes.DeleteFolderRequestModel
import com.pwhs.quickmem.domain.model.classes.DeleteStudySetsRequestModel
import com.pwhs.quickmem.domain.model.classes.ExitClassRequestModel
import com.pwhs.quickmem.domain.model.classes.InviteToClassRequestModel
import com.pwhs.quickmem.domain.model.classes.JoinClassRequestModel
import com.pwhs.quickmem.domain.model.classes.RemoveMembersRequestModel
import com.pwhs.quickmem.domain.repository.ClassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ClassDetailViewModel @Inject constructor(
    private val classRepository: ClassRepository,
    private val appManager: AppManager,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClassDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<ClassDetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val id: String = savedStateHandle.get<String>("id") ?: ""
        val title: String = savedStateHandle.get<String>("title") ?: ""
        val description: String = savedStateHandle.get<String>("description") ?: ""
        viewModelScope.launch {
            appManager.isLoggedIn.collect { isLoggedIn ->
                if (isLoggedIn) {
                    _uiState.update {
                        it.copy(
                            isLogin = true,
                            id = id,
                            title = title,
                            description = description
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLogin = false) }
                    onEvent(ClassDetailUiAction.NavigateToWelcomeClicked)
                }
            }
        }
        _uiState.update { it.copy(id = id) }
        getClassById()
        saveRecentAccessClass(classId = id)
    }

    fun onEvent(event: ClassDetailUiAction) {
        when (event) {
            is ClassDetailUiAction.Refresh -> {
                getClassById()
            }

            is ClassDetailUiAction.NavigateToWelcomeClicked -> {
                _uiEvent.trySend(ClassDetailUiEvent.NavigateToWelcome)
            }

            is ClassDetailUiAction.DeleteClass -> {
                deleteClass(id = _uiState.value.id)
                _uiEvent.trySend(ClassDetailUiEvent.ClassDeleted)
            }

            is ClassDetailUiAction.EditClass -> {
                _uiEvent.trySend(ClassDetailUiEvent.NavigateToEditClass)
            }

            is ClassDetailUiAction.OnNavigateToAddFolder -> {
                _uiEvent.trySend(ClassDetailUiEvent.OnNavigateToAddFolder)
            }

            is ClassDetailUiAction.OnNavigateToAddStudySets -> {
                _uiEvent.trySend(ClassDetailUiEvent.OnNavigateToAddStudySets)
            }

            is ClassDetailUiAction.ExitClass -> {
                exitClass()
            }

            is ClassDetailUiAction.NavigateToRemoveMembers -> {
                _uiEvent.trySend(ClassDetailUiEvent.OnNavigateToRemoveMembers)
            }

            is ClassDetailUiAction.OnDeleteMember -> {
                removeMember(event.memberId)
            }

            ClassDetailUiAction.OnJoinClass -> {
                joinClassByToken()
            }

            is ClassDetailUiAction.OnDeleteStudySetInClass -> {
                deleteStudySetInClass(event.studySetId)
            }

            is ClassDetailUiAction.OnDeleteFolderInClass -> {
                deleteFolderInClass(event.folderId)
            }

            is ClassDetailUiAction.OnChangeUsername -> {
                _uiState.update {
                    it.copy(
                        username = event.username,
                        errorMessage = null
                    )
                }
                if (event.username == _uiState.value.userResponseModel.username) {
                    _uiState.update {
                        it.copy(
                            errorMessage = R.string.txt_you_can_not_invite_yourself,
                            isInvited = false
                        )
                    }
                    return
                }
            }

            ClassDetailUiAction.OnInviteClass -> {
                inviteToClass()
            }
        }
    }

    private fun getClassById() {
        val id = _uiState.value.id
        viewModelScope.launch {
            classRepository.getClassById(classId = id).collectLatest { resource ->
                when (resource) {
                    is Resources.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }

                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resources.Success -> {
                        resource.data?.let { data ->
                            val isOwner = data.owner.id == appManager.userId.firstOrNull()
                            val isMember =
                                data.members?.any { it.id == appManager.userId.firstOrNull() } == true
                            _uiState.update {
                                it.copy(
                                    title = data.title,
                                    description = data.description,
                                    joinClassCode = data.joinToken,
                                    id = data.id,
                                    isLoading = false,
                                    isMember = isMember,
                                    isOwner = isOwner,
                                    isAllowManage = data.allowSetManagement,
                                    allowMember = data.allowMemberManagement,
                                    userResponseModel = data.owner,
                                    folders = data.folders ?: emptyList(),
                                    studySets = data.studySets ?: emptyList(),
                                    members = data.members ?: emptyList()
                                )
                            }
                        } ?: run {
                            _uiEvent.send(ClassDetailUiEvent.ShowError(R.string.txt_class_not_found))
                        }
                    }
                }
            }
        }
    }

    private fun deleteClass(id: String) {
        viewModelScope.launch {
            classRepository.deleteClass(classId = id).collectLatest { resource ->
                when (resource) {
                    is Resources.Error -> {
                        Timber.e(resource.message)
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resources.Success -> {
                        resource.data?.let {
                            _uiState.update {
                                it.copy(isLoading = false)
                            }
                            _uiEvent.send(ClassDetailUiEvent.ClassDeleted)
                        }
                    }
                }
            }
        }
    }

    private fun exitClass() {
        viewModelScope.launch {
            val classId = _uiState.value.id
            classRepository.exitClass(exitClassRequestModel = ExitClassRequestModel(classId = classId))
                .collectLatest { resource ->
                    when (resource) {
                        is Resources.Error -> {
                            Timber.e(resource.message)
                            _uiState.update {
                                it.copy(isLoading = false)
                            }
                        }

                        is Resources.Loading -> {
                            _uiState.update {
                                it.copy(isLoading = true)
                            }
                        }

                        is Resources.Success -> {
                            resource.data?.let {
                                Timber.d("Exited this class")
                                _uiState.update {
                                    it.copy(isLoading = false)
                                }
                                _uiEvent.send(ClassDetailUiEvent.ExitClass)
                            }
                        }
                    }
                }
        }
    }

    private fun removeMember(memberId: String) {
        viewModelScope.launch {
            val classId = _uiState.value.id
            classRepository.removeMembers(
                removeMembersRequestModel = RemoveMembersRequestModel(
                    classId = classId,
                    memberIds = listOf(memberId)
                )
            ).collectLatest { resource ->
                when (resource) {
                    is Resources.Error -> {
                        Timber.e(resource.message)
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resources.Success -> {
                        val members = _uiState.value.members.toMutableList()
                        members.removeAll { it.id == memberId }
                        resource.data?.let {
                            Timber.d("Member removed")
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    members = members
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun joinClassByToken() {
        viewModelScope.launch {
            val classId = _uiState.value.id
            val joinClassCode = _uiState.value.joinClassCode
            classRepository.joinClass(
                joinClassRequestModel = JoinClassRequestModel(
                    joinToken = joinClassCode,
                    classId = classId
                )
            ).collectLatest { resource ->
                when (resource) {
                    is Resources.Error -> {
                        Timber.e(resource.message)
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resources.Success -> {
                        getClassById()
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isMember = true
                            )
                        }
                        _uiEvent.trySend(ClassDetailUiEvent.OnJoinClass)
                    }
                }
            }
        }
    }

    private fun deleteStudySetInClass(studySetId: String) {
        viewModelScope.launch {
            val classId = _uiState.value.id
            classRepository.deleteStudySetInClass(
                deleteStudySetsRequestModel = DeleteStudySetsRequestModel(
                    classId = classId,
                    studySetId = studySetId
                )
            ).collectLatest { resource ->
                when (resource) {
                    is Resources.Error -> {
                        Timber.e(resource.message)
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resources.Success ->
                        resource.data?.let {
                            val studySets = _uiState.value.studySets.toMutableList()
                            studySets.removeAll { it.id == studySetId }
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    studySets = studySets
                                )
                            }
                        }
                }
            }
        }
    }

    private fun deleteFolderInClass(folderId: String) {
        viewModelScope.launch {
            val classId = _uiState.value.id
            classRepository.deleteFolderInClass(
                deleteFolderRequestModel = DeleteFolderRequestModel(
                    classId = classId,
                    folderId = folderId
                )
            ).collectLatest { resource ->
                when (resource) {
                    is Resources.Error -> {
                        Timber.e(resource.message)
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resources.Success ->
                        resource.data?.let {
                            val folders = _uiState.value.folders.toMutableList()
                            folders.removeAll { it.id == folderId }
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    folders = folders
                                )
                            }
                        }
                }
            }
        }
    }

    private fun saveRecentAccessClass(classId: String) {
        viewModelScope.launch {
            classRepository.saveRecentAccessClass(id = classId).collect()
        }
    }

    private fun inviteToClass() {
        viewModelScope.launch {
            val classId = _uiState.value.id
            val username = _uiState.value.username
            if (username.isEmpty()) {
                _uiState.update {
                    it.copy(
                        errorMessage = R.string.txt_username_cannot_be_empty
                    )
                }
                return@launch
            }
            if (username.length < 4) {
                _uiState.update {
                    it.copy(
                        errorMessage = R.string.txt_username_must_be_at_least_4_characters
                    )
                }
                return@launch
            }

            classRepository.inviteToClass(
                inviteToClassRequestModel = InviteToClassRequestModel(classId, username)
            ).collectLatest { resource ->
                when (resource) {
                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resources.Success -> {
                        Timber.d("Invite to class success status: ${resource.data?.inviteStatus}")
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isInvited = resource.data?.status == true,
                                errorMessage = when (resource.data?.inviteStatus) {
                                    "ALREADY_MEMBER" -> R.string.txt_this_user_is_already_a_member
                                    "IS_OWNER" -> R.string.txt_this_user_is_owner
                                    "ALREADY_INVITED" -> R.string.txt_this_user_is_already_invited
                                    "USER_NOT_VERIFIED" -> R.string.txt_this_user_is_not_verified
                                    "NOT_FOUND" -> R.string.txt_user_not_found
                                    "ALREADY_JOINED" -> R.string.txt_this_user_is_already_joined
                                    else -> null
                                }
                            )
                        }
                    }

                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = R.string.txt_error_occurred,
                                isLoading = false
                            )
                        }
                        _uiEvent.send(
                            ClassDetailUiEvent.ShowError(
                                R.string.txt_error_occurred
                            )
                        )
                    }
                }
            }
        }
    }
}
