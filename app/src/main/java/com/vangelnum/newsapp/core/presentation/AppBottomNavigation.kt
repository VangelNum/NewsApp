package com.vangelnum.newsapp.core.presentation

import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
fun AppBottomNavigation(
    items: List<Screens>,
    currentDestination: NavDestination?,
    navController: NavController
) {
    BottomNavigation {
        items.forEach { screen ->
            BottomNavigationItem(selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painterResource(id = screen.icon),
                        contentDescription = "icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(text = screen.title)
                },
                alwaysShowLabel = true
            )
        }
    }
}