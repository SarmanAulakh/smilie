package com.example.smilie


import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smilie.screens.HomeScreen
import com.example.smilie.model.view.MainViewModel
import com.example.smilie.screens.chat.ChatScreen
import com.example.smilie.model.view.ProfileViewModel
import com.example.smilie.screens.rate.RateYourDay
import com.example.smilie.screens.login.LoginScreen
import com.example.smilie.screens.profile.ProfileScreen
import com.example.smilie.screens.rate.AddMetrics
import com.example.smilie.screens.rate.CustomMetrics
import com.example.smilie.screens.rate.RemoveMetrics
import com.example.smilie.screens.settings.SettingsManager
import com.example.smilie.screens.settings.SettingsScreen
import com.example.smilie.screens.sign_up.SignUpScreen
import com.example.smilie.screens.sign_up.UserRegisterScreen
import com.example.smilie.ui.navigation.ADD_METRICS_SCREEN
import com.example.smilie.ui.navigation.BottomNavBar
import com.example.smilie.ui.navigation.ChatResources
import com.example.smilie.ui.navigation.Home
import com.example.smilie.ui.navigation.LOGIN_SCREEN
import com.example.smilie.ui.navigation.Profile
import com.example.smilie.ui.navigation.RateYourDay
import com.example.smilie.ui.navigation.REMOVE_METRICS_SCREEN
import com.example.smilie.ui.navigation.SIGN_UP_SCREEN
import com.example.smilie.ui.navigation.Settings
import com.example.smilie.ui.navigation.USER_REGISTER_SCREEN
import com.example.smilie.ui.navigation.CUSTOM_METRICS_SCREEN
import com.example.smilie.ui.navigation.smilieTabRowScreens
import com.example.smilie.ui.theme.SMILIETheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext


// REFERENCE: https://github.com/android/codelab-android-compose/blob/main/NavigationCodelab
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    viewModel: MainViewModel = hiltViewModel()
) {
    val isUserSignedIn = FirebaseAuth.getInstance().currentUser != null
    val startingRoute = if (isUserSignedIn) Home.route else LOGIN_SCREEN

    val settingsManager = SettingsManager(initialDark=true, initialFontSize=2f)

    SMILIETheme(settingsManager) {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = smilieTabRowScreens.find { it.route == currentDestination?.route?.split("/|\\?".toRegex())?.get(0) } ?: Home
        var showBottomNav by remember {mutableStateOf(false)}

        Scaffold(
            bottomBar = {
                // null if current route != row tab route options
                if (showBottomNav) {
                    BottomNavBar(
                        allScreens = smilieTabRowScreens,
                        onTabSelected = { newScreen ->
                            navController.navigateSingleTopTo(newScreen.route)
                        },
                        currentScreen = currentScreen
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = startingRoute,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(route = Home.route) {

                    showBottomNav = true
                    viewModel.getUser();
                    var userData = viewModel.userData.value
                    viewModel.getAllUsers()
                    var allUserData = viewModel.allUsers.value
                    HomeScreen(user=userData, allUsers=allUserData)
                   // HomeScreen(user=userData, metrics=metricData, allUsers=allUserData)
                }
                composable(
                    route = Profile.route + "?userId={userId}",
                    arguments = listOf(
                        navArgument("userId") {
                            type = NavType.StringType
                            defaultValue = viewModel.userData.value?.id
                            nullable = true
                        }
                    )
                ) { entry ->
                    showBottomNav = true
                    ProfileScreen(
                        userId = entry.arguments?.getString("userId"),
                        openAndPopUp = { route -> navController.navigateSingleTopTo(route) },
                        settingsManager=settingsManager
                    )
                }
                composable(route = Settings.route) {
                    showBottomNav = true
                    viewModel.getMetrics()
                    var metricData = viewModel.metricData.value

                    viewModel.getUser()
                    var userData = viewModel.userData.value
                    SettingsScreen(
                        openAndPopUp = { route -> navController.navigateSingleTopTo(route) },
                        metrics = metricData,
                        user = userData,
                        settingsManager = settingsManager,
                    )
                }
                composable(route = ChatResources.route) {
                    showBottomNav = true
                    viewModel.getMetrics()
                    var metricData = viewModel.metricData.value
                    ChatScreen(
                        metrics = metricData,
                    )
                }
                composable(LOGIN_SCREEN) {
                    showBottomNav = false
                    LoginScreen(openAndPopUp = {
                        navController.navigateSingleTopTo(it)
                        viewModel.userData.value
                    })
                }
                composable(SIGN_UP_SCREEN) {
                    showBottomNav = false
                    SignUpScreen(openAndPopUp = {
                        route -> navController.navigateSingleTopTo(route)
                        viewModel.userData.value
                    })
                }
                composable(USER_REGISTER_SCREEN) {
                    showBottomNav = false
                    UserRegisterScreen(
                        openAndPopUp = { route -> navController.navigateSingleTopTo(route) },
                    )
                }
                composable(route = RateYourDay.route) {
                    showBottomNav = true
                    viewModel.getMetrics()
                    var metricData = viewModel.metricData.value
                    RateYourDay(
                        openAndPopUp = { route -> navController.navigateSingleTopTo(route) },
                        metrics = metricData
                    )
                }
                composable(REMOVE_METRICS_SCREEN) {
                    showBottomNav = false
                    viewModel.getMetrics()
                    var metricData = viewModel.metricData.value
                    RemoveMetrics(
                        openAndPopUp = { route -> navController.navigateSingleTopTo(route) },
                        metrics = metricData
                    )
                }
                composable(ADD_METRICS_SCREEN) {
                    showBottomNav = false
                    viewModel.getMetrics()
                    var metricData = viewModel.metricData.value
                    AddMetrics(
                        openAndPopUp = { route -> navController.navigateSingleTopTo(route) },
                        metrics = metricData
                    )
                }
                composable(CUSTOM_METRICS_SCREEN) {
                    showBottomNav = false
                    CustomMetrics(
                        openAndPopUp = { route -> navController.navigateSingleTopTo(route) }
                    )
                }
            }
        }
    }
}

// REFERENCE: https://github.com/android/codelab-android-compose/blob/main/NavigationCodelab
fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // This isn't behaving correctly or I'm misunderstanding something. It's probably the latter.
        // But it works after I comment it out so that's what I'll do for now
//        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
//            saveState = true
//        }
        launchSingleTop = true
        restoreState = true
    }