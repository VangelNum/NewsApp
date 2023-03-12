package com.vangelnum.newsapp.core.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vangelnum.newsapp.FilterScreen
import com.vangelnum.newsapp.core.presentation.drawer_layout.DrawerBody
import com.vangelnum.newsapp.core.presentation.drawer_layout.DrawerHeader
import com.vangelnum.newsapp.feature_contact.presentation.ContactScreen
import com.vangelnum.newsapp.feature_favourite.presentation.FavouriteScreen
import com.vangelnum.newsapp.feature_favourite.presentation.FavouriteViewModel
import com.vangelnum.newsapp.feature_main.presentation.MainScreen
import com.vangelnum.newsapp.feature_search.presentation.SearchScreen


@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    viewModelFavourite: FavouriteViewModel = hiltViewModel()
) {
    val favouriteState = viewModelFavourite.readAllData.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            AppBottomNavigation(
                currentDestination = currentDestination,
                navController = navController
            )
        },
        topBar = {
            if (currentDestination?.route!=Screens.ContactScreen.route) {
                AppTopBar(scope, scaffoldState)
            } else {
                TopAppBar(title = { }, navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, "back")
                    }
                })
            }
        },
        drawerContent = {
            DrawerHeader()
            DrawerBody(navController = navController,scaffoldState.drawerState)
        },
        drawerShape = RoundedCornerShape(bottomEnd = 25.dp, topEnd = 25.dp)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.MainScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screens.MainScreen.route) {
                MainScreen(viewModelFavourite, favouriteState = favouriteState)
            }
            composable(route = Screens.FavouriteScreen.route) {
                FavouriteScreen(
                    favouriteViewModel = viewModelFavourite,
                    favouriteState = favouriteState
                )
            }
            composable(route = Screens.SearchScreen.route) {
                SearchScreen(favouriteState = favouriteState, favouriteViewModel = viewModelFavourite)
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
            composable(route = Screens.ContactScreen.route) {
                ContactScreen()
            }
        }
    }
}