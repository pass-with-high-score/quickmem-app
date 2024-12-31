package com.pwhs.quickmem.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<LoginUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: LoginUiAction) {
        when (event) {
            is LoginUiAction.LoginWithGoogle -> loginWithGoogle()
            is LoginUiAction.LoginWithFacebook -> loginWithFacebook()
            is LoginUiAction.LoginWithEmail -> loginWithEmail()
            is LoginUiAction.NavigateToSignUp -> navigateToSignUp()
        }
    }

    private fun loginWithGoogle() {
        viewModelScope.launch {
            _uiEvent.trySend(LoginUiEvent.LoginWithGoogle)
        }
    }

    private fun loginWithFacebook() {
        viewModelScope.launch {
            _uiEvent.trySend(LoginUiEvent.LoginWithFacebook)
        }
    }

    private fun loginWithEmail() {
        viewModelScope.launch {
            _uiEvent.trySend(LoginUiEvent.LoginWithEmail)
        }
    }

    private fun navigateToSignUp() {
        viewModelScope.launch {
            _uiEvent.trySend(LoginUiEvent.NavigateToSignUp)
        }
    }
}