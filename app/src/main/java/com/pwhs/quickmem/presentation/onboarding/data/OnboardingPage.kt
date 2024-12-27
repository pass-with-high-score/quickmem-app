package com.pwhs.quickmem.presentation.onboarding.data

import androidx.annotation.StringRes
import com.pwhs.quickmem.R

data class OnboardingPage(
    @StringRes val title: Int,
    @StringRes val description: Int,
)

val onboardingPagesList = listOf(
    OnboardingPage(
        title = R.string.txt_onboarding_title1,
        description = R.string.txt_onboarding_description1
    ),
    OnboardingPage(
        title = R.string.txt_onboarding_title2,
        description = R.string.txt_onboarding_description2
    ),
    OnboardingPage(
        title = R.string.txt_onboarding_title3,
        description = R.string.txt_onboarding_description3
    )
)