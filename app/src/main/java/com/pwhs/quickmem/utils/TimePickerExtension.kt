package com.pwhs.quickmem.utils

import android.icu.util.Calendar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerState.toFormattedString(): String {
    val localTime = LocalTime.of(this.hour, this.minute)
    val pattern = "HH:mm"
    return localTime.format(DateTimeFormatter.ofPattern(pattern))
}

@OptIn(ExperimentalMaterial3Api::class)
fun String.toTimePickerState(): TimePickerState {
    try {
        val localTime = LocalTime.parse(this, DateTimeFormatter.ofPattern("HH:mm"))
        return TimePickerState(
            initialMinute = localTime.minute,
            initialHour = localTime.hour,
            is24Hour = true
        )
    } catch (e: Exception) {
        val currentTime = Calendar.getInstance()
        return TimePickerState(
            initialMinute = currentTime.get(Calendar.MINUTE),
            initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
            is24Hour = true
        )
    }
}