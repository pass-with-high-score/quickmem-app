package com.pwhs.quickmem

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pwhs.quickmem.core.datastore.AppManager
import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.presentation.StandardScaffold
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.utils.launchInAppReview
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.generated.destinations.LibraryScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.generated.destinations.WelcomeScreenDestination
import com.ramcosta.composedestinations.rememberNavHostEngine
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var appManager: AppManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            QuickMemTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                val navController = rememberNavController()
                val navHostEngine = rememberNavHostEngine(
                    navHostContentAlignment = Alignment.TopCenter,
                )

                val newBackStackEntry by navController.currentBackStackEntryAsState()
                val route = newBackStackEntry?.destination?.route
                // check token valid when user already logged in
                val token = tokenManager.accessToken.collectAsState(initial = null)
                val isLogged = appManager.isLoggedIn.collectAsState(initial = false)
                val postLoginOpenCount = appManager.postLoginOpenCount.collectAsState(initial = 0)
                val reviewPromptShown = appManager.reviewPromptShown.collectAsState(initial = false)
                val scope = rememberCoroutineScope()
                LaunchedEffect(key1 = token.value, key2 = isLogged.value) {
                    if (isLogged.value && token.value == null) {
                        appManager.clearAllData()
                        navController.navigate(WelcomeScreenDestination) {
                            popUpTo(NavGraphs.root) {
                                saveState = false
                            }
                            launchSingleTop = true
                            restoreState = false
                        }
                    }
                }
                LaunchedEffect(key1 = true) {
                    appManager.incrementPostLoginOpenCount()
                }
                LaunchedEffect(key1 = postLoginOpenCount.value, key2 = reviewPromptShown.value) {
                    if (postLoginOpenCount.value == 2 && !reviewPromptShown.value) {
                        context.launchInAppReview { success ->
                            // Mark as shown regardless of success to avoid nagging
                            scope.launch {
                                appManager.setReviewPromptShown(true)
                            }
                        }
                    }
                }
                StandardScaffold(
                    navController = navController,
                    showBottomBar = route in listOf(
                        HomeScreenDestination.route,
                        LibraryScreenDestination.route,
                        ProfileScreenDestination.route,
                    ),
                    content = {
                        DestinationsNavHost(
                            navGraph = NavGraphs.root,
                            navController = navController,
                            engine = navHostEngine,
                        )
                    }
                )
            }
        }
        @SuppressLint("SourceLockedOrientationActivity")
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}
