package com.example.e_store.utils.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Sign_up : Screen("sign_up")
    object Sign_in : Screen("sign_in")
}
