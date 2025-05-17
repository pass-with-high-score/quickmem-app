package com.pwhs.quickmem.presentation.app.home.recent.folder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.repository.FolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllRecentAccessFoldersViewModel @Inject constructor(
    private val folderRepository: FolderRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AllRecentAccessFoldersUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<AllRecentAccessFoldersUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getAllRecentAccessFolders()
    }

    private fun getAllRecentAccessFolders() {
        viewModelScope.launch {
            folderRepository.getRecentAccessFolders().collect { resource ->
                when (resource) {
                    is Resources.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is Resources.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            folders = resource.data ?: emptyList()
                        )
                    }

                    is Resources.Error -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                }
            }
        }
    }
}