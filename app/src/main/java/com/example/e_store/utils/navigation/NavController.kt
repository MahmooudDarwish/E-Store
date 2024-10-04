package com.example.e_store.utils.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.e_store.features.authentication.Sign_in_Screen
import com.example.e_store.features.authentication.Sign_up_Screen
import com.example.e_store.features.home.view.HomeScreen


@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            Sign_in_Screen(navController = navController)
        }
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Sign_up.route) {
            Sign_up_Screen(navController = navController)
        }
    }
}