package com.pwhs.quickmem.presentation.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.repository.AuthRepository
import com.pwhs.quickmem.utils.isInternetAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appManager: AppManager,
    private val tokenManager: TokenManager,
    private val authRepository: AuthRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val _uiEvent = Channel<SplashUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _isInternetAvailable = MutableStateFlow(true)
    val isInternetAvailable get() = _isInternetAvailable.asStateFlow()

    init {
        if (isInternetAvailable(getApplication())) {
            checkAuth()
        } else {
            _isInternetAvailable.value = false
            _uiEvent.trySend(SplashUiEvent.NoInternet)
        }
    }

    private fun checkAuth() {
        viewModelScope.launch {
            delay(3000)
            appManager.isLoggedIn.collect { isLoggedIn ->
                if (isLoggedIn) {
                    getUserProfile()
                } else {
                    checkFirstRun()
                }
            }
        }
    }

    private fun checkFirstRun() {
        viewModelScope.launch {
            val isFirstRun = appManager.isFirstRun.firstOrNull() != false
            if (isFirstRun) {
                appManager.saveIsFirstRun(true)
                _uiEvent.send(SplashUiEvent.FirstRun)
            } else {
                _uiEvent.send(SplashUiEvent.NotFirstRun)
            }
        }
    }

    fun retry() {
        if (isInternetAvailable(getApplication())) {
            checkAuth()
        } else {
            _uiEvent.trySend(SplashUiEvent.NoInternet)
        }
    }

    private fun getUserProfile() {
        viewModelScope.launch {

            authRepository.getUserProfile().collectLatest { resource ->
                when (resource) {
                    is Resources.Loading -> {

                    }

                    is Resources.Success -> {
                        resource.data?.let { data ->
                            appManager.saveUserName(data.username)
                            appManager.saveUserAvatar(data.avatarUrl)
                            appManager.saveUserFullName(data.fullname)
                            appManager.saveUserEmail(data.email)
                            appManager.saveUserId(data.id)
                            appManager.saveUserCoins(data.coin)
                            appManager.saveUserCreatedAt(data.createdAt)
                            appManager.saveUserCoins(data.coin)
                        }
                        _uiEvent.send(SplashUiEvent.IsLoggedIn)
                    }

                    is Resources.Error -> {
                        appManager.clearAllData()
                        tokenManager.clearTokens()
                        _uiEvent.send(SplashUiEvent.NotLoggedIn)
                        Timber.e("Error fetching profile: ${resource.message}")
                    }
                }
            }
        }
    }
}