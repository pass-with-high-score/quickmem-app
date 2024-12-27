package com.pwhs.quickmem.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.pwhs.quickmem.R
import com.pwhs.quickmem.presentation.app.home.components.StreakCalendar
import com.pwhs.quickmem.presentation.component.BottomSheetItem
import com.pwhs.quickmem.ui.theme.streakTextColor
import com.pwhs.quickmem.ui.theme.streakTitleColor
import com.ramcosta.composedestinations.generated.destinations.CreateClassScreenDestination
import com.ramcosta.composedestinations.generated.destinations.CreateFolderScreenDestination
import com.ramcosta.composedestinations.generated.destinations.CreateStudySetScreenDestination
import com.ramcosta.composedestinations.utils.isRouteOnBackStackAsState
import com.ramcosta.composedestinations.utils.rememberDestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardScaffold(
    navController: NavController,
    showBottomBar: Boolean,
    items: List<BottomNavItem> = listOf(
        BottomNavItem.Home,
        BottomNavItem.Explore,
        BottomNavItem.Center,
        BottomNavItem.Library,
        BottomNavItem.Profile
    ),
    content: @Composable (PaddingValues) -> Unit,
    viewModel: StandardViewModel = hiltViewModel(),
) {
    val sheetSelectCreateState = rememberModalBottomSheetState()
    var showBottomSheetCreate by remember {
        mutableStateOf(false)
    }
    val navigator = navController.rememberDestinationsNavigator()
    val streakBottomSheet = rememberModalBottomSheetState()
    var showStreakBottomSheet by remember {
        mutableStateOf(false)
    }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_fire_streak))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column {
                    NavigationBar(
                        modifier = Modifier
                            .shadow(4.dp, RoundedCornerShape(0.dp))
                            .background(colorScheme.surface),
                        containerColor = colorScheme.background,
                        contentColor = colorScheme.onBackground,
                        content = {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            items.forEach { item ->
                                val isCurrentDestOnBackStack by navController.isRouteOnBackStackAsState(
                                    item.direction
                                )
                                val color by animateColorAsState(
                                    targetValue = if (currentDestination?.route?.contains(item.route) == true) {
                                        colorScheme.primary
                                    } else {
                                        colorScheme.onBackground
                                    },
                                    label = "color_anim"
                                )
                                val iconScale by animateFloatAsState(
                                    targetValue = if (currentDestination?.route?.contains(item.route) == true) {
                                        1.2f
                                    } else {
                                        1f
                                    },
                                    label = "scale_anim"
                                )
                                NavigationBarItem(
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = colorScheme.primary,
                                        unselectedIconColor = colorScheme.onBackground,
                                        selectedTextColor = colorScheme.primary,
                                        unselectedTextColor = colorScheme.onBackground,
                                        indicatorColor = Color.Transparent,
                                    ),
                                    icon = {
                                        Icon(
                                            modifier = if (item.route == "fab") {
                                                Modifier
                                                    .size(40.dp)
                                                    .scale(iconScale)
                                            } else {
                                                Modifier
                                                    .size(25.dp)
                                                    .scale(iconScale)
                                            },
                                            painter = painterResource(id = item.icon),
                                            contentDescription = stringResource(item.title),
                                            tint = color
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = if (item.route == "fab") {
                                                ""
                                            } else {
                                                stringResource(item.title)
                                            },
                                            maxLines = 1,
                                            style = typography.bodySmall.copy(
                                                color = color,
                                                fontWeight = if (currentDestination?.route?.contains(
                                                        item.route
                                                    ) == true
                                                ) {
                                                    FontWeight.Bold
                                                } else {
                                                    FontWeight.Normal
                                                },
                                                fontSize = 10.sp
                                            ),
                                        )
                                    },
                                    alwaysShowLabel = true,
                                    selected = currentDestination?.route?.contains(item.route) == true,
                                    onClick = {
                                        if (item.route == "fab") {
                                            showBottomSheetCreate = true
                                        } else {
                                            if (isCurrentDestOnBackStack) {
                                                navigator.popBackStack(item.direction, false)
                                                return@NavigationBarItem
                                            }
                                            navigator.navigate(item.direction) {
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            if (showBottomBar) {
                Card(
                    onClick = {
                        showStreakBottomSheet = true
                    },
                    shape = CircleShape,
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 8.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = colorScheme.surface
                    ),
                    border = BorderStroke(
                        width = 2.dp,
                        color = Color.White
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(5.dp)
                            .padding(horizontal = 8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_fire),
                            contentDescription = stringResource(R.string.txt_streak),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            text = "${uiState.streakCount}",
                            style = typography.titleLarge.copy(
                                color = colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Start,
    ) { innerPadding ->
        content(innerPadding)
        if (showBottomSheetCreate) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheetCreate = false
                },
                sheetState = sheetSelectCreateState,
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetItem(
                        title = stringResource(R.string.txt_study_set),
                        icon = R.drawable.ic_card,
                        onClick = {
                            showBottomSheetCreate = false
                            navController.navigate(CreateStudySetScreenDestination.route)
                        }
                    )
                    BottomSheetItem(
                        title = stringResource(R.string.txt_folder),
                        icon = R.drawable.ic_folder,
                        onClick = {
                            showBottomSheetCreate = false
                            navController.navigate(CreateFolderScreenDestination.route)
                        }
                    )
                    BottomSheetItem(
                        title = stringResource(R.string.txt_class),
                        icon = R.drawable.ic_school,
                        onClick = {
                            showBottomSheetCreate = false
                            navController.navigate(CreateClassScreenDestination.route)
                        }
                    )
                }
            }
        }
        if (showStreakBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showStreakBottomSheet = false
                },
                sheetState = streakBottomSheet,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                    )
                    Text(
                        text = "${uiState.streakCount}",
                        style = typography.titleLarge.copy(
                            color = streakTitleColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 52.sp
                        )
                    )
                    Text(
                        text = when (uiState.streakCount) {
                            1 -> stringResource(R.string.txt_day_streak)
                            else -> stringResource(R.string.txt_days_streak)
                        },
                        style = typography.titleLarge.copy(
                            color = streakTextColor,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = stringResource(R.string.txt_practice_every_day),
                    )
                    StreakCalendar(
                        streakDates = uiState.streakDates
                    )
                }
            }
        }
    }
}

