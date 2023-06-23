package com.example.smilie.ui.components.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun BottomNavBar(
    allScreens: List<NavDestination>,
    onTabSelected: (NavDestination) -> Unit,
    currentScreen: NavDestination
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
