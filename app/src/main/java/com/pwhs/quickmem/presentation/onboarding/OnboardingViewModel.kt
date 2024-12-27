package com.pwhs.quickmem.presentation.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.core.datastore.AppManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val appManager: AppManager,
    application: Application,
) : AndroidViewModel(application) {
    fun saveIsFirstRun(isFirstRun: Boolean) {
        viewModelScope.launch {
            appManager.saveIsFirstRun(isFirstRun)
        }
    }
}