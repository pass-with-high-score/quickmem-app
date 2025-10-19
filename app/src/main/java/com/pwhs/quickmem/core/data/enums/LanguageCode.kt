package com.pwhs.quickmem.core.data.enums

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pwhs.quickmem.R

enum class LanguageCode(
    val code: String,
    @StringRes val displayName: Int,
    @DrawableRes val icon: Int
) {
    EN(
        "en",
        R.string.txt_english_us,
        R.drawable.ic_us_flag
    ),
    VI(
        "vi",
        R.string.txt_vietnamese,
        R.drawable.ic_vn_flag
    )
}