package com.example.smilie.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth

// REFERENCE: https://itnext.io/navigation-bar-bottom-app-bar-in-jetpack-compose-with-material-3-c57ae317bd00

@Composable
fun BottomNavBar(
    allScreens: List<NavDestination>,
    onTabSelected: (NavDestination) -> Unit,
    currentScreen: NavDestination,
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.background) {
        allScreens.forEach { screen ->
            NavigationBarItem(
                selected = screen.route == currentScreen.route,
                onClick = { onTabSelected(screen) },
                label = {
                    Text(
                        text = screen.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = "${screen.name} Icon",
                    )
                }
            )
        }

    }
}
