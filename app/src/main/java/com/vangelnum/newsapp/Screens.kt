package com.vangelnum.newsapp

sealed class Screens(val icon: Int, val route: String,val title: String) {
    object MainScreen: Screens(R.drawable.ic_baseline_home_24,"main_screen","Popular")
    object FavouriteScreen: Screens(R.drawable.ic_baseline_favorite_24,"favourite_screen","Favourite")
    object SearchScreen: Screens(R.drawable.ic_baseline_search_24,"search_screen","Search")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
