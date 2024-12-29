package com.pwhs.quickmem.presentation

sealed class StandardUiAction {
    data object UpdateStreak : StandardUiAction()
}