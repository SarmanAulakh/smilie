package com.example.smilie.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

// REFERENCE: https://github.com/android/codelab-android-compose/blob/main/NavigationCodelab
// SPECIFICALLY: https://github.com/android/codelab-android-compose/blob/main/NavigationCodelab/app/src/main/java/com/example/compose/rally/RallyDestinations.kt

interface NavDestination {
    val name: String
    val route: String
    val icon: ImageVector
}

object Home : NavDestination {
    override val name = "Home"
    override val route = "home"
    override val icon = Icons.Rounded.Home
}

object Profile : NavDestination {
    override val name = "Profile"
    override val route = "profile"
    override val icon = Icons.Rounded.AccountCircle
}

object Settings : NavDestination {
    override val name = "Settings"
    override val route = "settings"
    override val icon = Icons.Rounded.Settings
}

object RateYourDay : NavDestination {
    override val name = "Rate"
    override val route = "RateYourDay"
    override val icon = Icons.Rounded.Add
}

object ChatResources : NavDestination {
    override val name = "Chatbot"
    override val route = "ChatResources"
    override val icon = Icons.Rounded.Chat
}

// tab routes
val smilieTabRowScreens = listOf(Home, Profile, RateYourDay, ChatResources, Settings)

// non-tab routes
const val LOGIN_SCREEN = "LoginScreen"
const val SIGN_UP_SCREEN = "SignUpScreen"
const val USER_REGISTER_SCREEN = "UserRegisterScreen"
const val REMOVE_METRICS_SCREEN = "RemoveMetricsScreen"
const val ADD_METRICS_SCREEN = "AddMetricsScreen"
const val CUSTOM_METRICS_SCREEN = "CustomMetricsScreen"