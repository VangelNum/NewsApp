package com.vangelnum.newsapp.core.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vangelnum.newsapp.FilterScreen
import com.vangelnum.newsapp.feature_favourite.presentation.FavouriteScreen
import com.vangelnum.newsapp.feature_favourite.presentation.FavouriteViewModel
import com.vangelnum.newsapp.feature_main.presentation.MainScreen
import com.vangelnum.newsapp.feature_search.presentation.SearchScreen


@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    viewModelFavourite: FavouriteViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            AppBottomNavigation(
                currentDestination = currentDestination,
                navController = navController
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.MainScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screens.MainScreen.route) {
                MainScreen(viewModelFavourite)
            }
            composable(route = Screens.FavouriteScreen.route) {
                FavouriteScreen()
            }
            composable(route = Screens.SearchScreen.route) {
                SearchScreen(
                    scaffoldState = scaffoldState,
                    navController = navController
                )
            }
            composable(route = Screens.FilterScreen.route + "/{query}/{sortBy}", arguments =
            listOf(
                navArgument("query") {
                    type = NavType.StringType
                },
                navArgument("sortBy") {
                    type = NavType.StringType
                }
            )
            ) { entry ->
                FilterScreen(
                    query = entry.arguments?.getString("query"),
                    sortBy = entry.arguments?.getString("sortBy"),
                    navController = navController
                )
            }
        }
    }
}