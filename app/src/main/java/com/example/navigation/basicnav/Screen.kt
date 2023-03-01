package com.example.navigation.basicnav

sealed class Screen(val route: String) {
    object MainScreen : Screen("main-screen")
    object DetailScreen : Screen("detail-screen")
    object DetailScreenDeepLink : Screen("detail-screen-deeplink")


    // this only works for only mandatory ones

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { argument ->
                append("/${argument}")
            }
        }
    }
}
