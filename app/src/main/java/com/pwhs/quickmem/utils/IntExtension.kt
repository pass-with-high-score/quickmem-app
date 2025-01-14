package com.pwhs.quickmem.utils

import android.content.Context
import com.pwhs.quickmem.R

fun Int.toTimeString(context: Context): String {

    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60


    val hourString = if (hours > 0) {
        context.resources.getQuantityString(R.plurals.hours, hours, hours)
    } else {
        null
    }
    val minuteString = if (minutes > 0) {
        context.resources.getQuantityString(R.plurals.minutes, minutes, minutes)
    } else {
        null
    }
    val secondString = if (seconds > 0) {
        context.resources.getQuantityString(R.plurals.seconds, seconds, seconds)
    } else {
        null
    }

    val result = listOfNotNull(hourString, minuteString, secondString).joinToString(" ")
    if (result.isEmpty()) {
        return context.getString(R.string.txt_no_data)
    }
    return result
}
