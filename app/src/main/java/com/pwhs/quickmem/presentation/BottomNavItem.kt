package com.pwhs.quickmem.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pwhs.quickmem.R
import com.ramcosta.composedestinations.generated.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.generated.destinations.LibraryScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.spec.Direction

sealed class BottomNavItem(
    @param:StringRes val title: Int,
    @param:DrawableRes val icon: Int,
    val route: String,
    val direction: Direction,
) {
    data object Home : BottomNavItem(
        title = R.string.txt_home,
        icon = R.drawable.ic_home,
        route = HomeScreenDestination.route,
        direction = HomeScreenDestination()
    )

    data object Library : BottomNavItem(
        title = R.string.txt_library,
        icon = R.drawable.ic_library,
        route = LibraryScreenDestination.route,
        direction = LibraryScreenDestination()
    )

    data object Profile : BottomNavItem(
        title = R.string.txt_profile,
        icon = R.drawable.ic_person,
        route = ProfileScreenDestination.route,
        direction = ProfileScreenDestination()
    )
}