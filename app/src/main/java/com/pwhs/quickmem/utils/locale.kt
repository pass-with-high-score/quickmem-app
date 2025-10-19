package com.pwhs.quickmem.utils

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

const val FOLLOW_SYSTEM = "Language#FollowSystem"

fun changeLanguage(language: String) {
    if (language == FOLLOW_SYSTEM) {
        // Set empty locale list to follow system
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
    } else {
        val locale = Locale.forLanguageTag(language)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale))
    }
}
