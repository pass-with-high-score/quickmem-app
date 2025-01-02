package com.pwhs.quickmem.utils

import androidx.compose.runtime.Composable

@Composable
fun Int.each(block: @Composable (Int) -> Unit) {
    for (i in 0 until this) {
        block(i)
    }
}