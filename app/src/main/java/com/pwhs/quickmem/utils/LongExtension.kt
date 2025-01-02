package com.pwhs.quickmem.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Long.toFormattedString(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = Date(this)
    return dateFormat.format(date)
}

fun Long.isDateSmallerThan(): Boolean {
    val calendarBirth = Calendar.getInstance().apply { timeInMillis = this@isDateSmallerThan }
    val calendarCompare = Calendar.getInstance()

    var age = calendarCompare.get(Calendar.YEAR) - calendarBirth.get(Calendar.YEAR)

    if (calendarCompare.get(Calendar.DAY_OF_YEAR) < calendarBirth.get(Calendar.DAY_OF_YEAR)) {
        age--
    }
    return age < 20
}

fun Long.toStringTime(): String {
    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
}