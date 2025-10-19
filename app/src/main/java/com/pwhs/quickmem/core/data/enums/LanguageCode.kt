package com.pwhs.quickmem.core.data.enums

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pwhs.quickmem.R

enum class LanguageCode(
    val code: String,
    @param:StringRes val displayName: Int,
    @param:DrawableRes val icon: Int
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