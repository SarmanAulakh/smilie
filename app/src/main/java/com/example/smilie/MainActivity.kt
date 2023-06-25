package com.example.smilie

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.smilie.annotations.DarkLightPreviews
import com.example.smilie.screens.HomeScreen
import com.example.smilie.screens.ProfileScreen
import com.example.smilie.screens.SettingsScreen
import com.example.smilie.screens.login.LoginScreen
import com.example.smilie.ui.components.navigation.BottomNavBar
import com.example.smilie.ui.components.navigation.Home
import com.example.smilie.ui.components.navigation.LOGIN_SCREEN
import com.example.smilie.ui.components.navigation.Profile
import com.example.smilie.ui.components.navigation.Settings
import com.example.smilie.ui.components.navigation.smilieTabRowScreens
import com.example.smilie.ui.theme.SMILIETheme
import dagger.hilt.android.AndroidEntryPoint
import android.app.Application
import androidx.activity.ComponentActivity
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SmilieHiltApp : Application() {}

@AndroidEntryPoint
@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp()
        }
    }
}

// REFERENCE: https://github.com/android/codelab-android-compose/blob/main/NavigationCodelab
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    SMILIETheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = smilieTabRowScreens.find { it.route == currentDestination?.route } ?: Home

        Scaffold(
            bottomBar = {
                BottomNavBar(
                    allScreens = smilieTabRowScreens,
                    onTabSelected = { newScreen ->
                        navController.navigateSingleTopTo(newScreen.route)
                    },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = LOGIN_SCREEN,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(route = Home.route) {
                    HomeScreen()
                }
                composable(route = Profile.route) {
                    ProfileScreen()
                }
                composable(route = Settings.route) {
                    SettingsScreen()
                }
                composable(LOGIN_SCREEN) {
                    LoginScreen(openAndPopUp = { route -> navController.navigateSingleTopTo(route) })
                }
//                composable(SIGN_UP_SCREEN) {
//                    SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
//                }
            }
        }
    }
}

// REFERENCE: https://github.com/android/codelab-android-compose/blob/main/NavigationCodelab
fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }


@DarkLightPreviews
@Composable
fun FavoriteCollectionsGridPreview() {
    MainApp()
}