package com.vangelnum.newsapp

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val photos by viewModel.items.collectAsState()
    val news = viewModel.readAllData.observeAsState(listOf()).value


    val items = listOf(
        Screens.MainScreen,
        Screens.FavouriteScreen,
        Screens.SearchScreen
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        topBar = {
            if (navBackStackEntry?.destination?.route != "search_screen") {
                TopAppBar(
                ) {

                }
            }
        },
        bottomBar = {
            BottomAppBar(
                cutoutShape = MaterialTheme.shapes.small.copy(
                    CornerSize(percent = 50)
                )
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
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.MainScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screens.MainScreen.route) {
                MainScreen(photos = photos, viewModel = viewModel, news = news)
            }
            composable(route = Screens.FavouriteScreen.route) {
                FavouriteScreen(viewModel = viewModel, news = news)
            }
            composable(route = Screens.SearchScreen.route) {
                SearchScreen(viewModel = viewModel, news = news)
            }
        }
    }
}