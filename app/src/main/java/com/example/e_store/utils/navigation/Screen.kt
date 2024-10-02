package com.example.e_store.utils.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.e_store.R
import com.example.e_store.features.categories.view.CategoriesScreen
import com.example.e_store.features.home.view.HomeScreen
import com.example.e_store.features.profile.view.ProfileScreen
import com.example.e_store.features.shopping_cart.view.ShoppingCartScreen

sealed class Screen(val route: String, val title: String, val icon: Int) {
    object Splash : Screen("splash", "Splash", 0)
    object Home : Screen("home", "Home", R.drawable.ic_home)
    object Categories : Screen("categories", "Categories", R.drawable.ic_categories)
    object Cart : Screen("cart", "Cart", R.drawable.ic_shopping_cart)
    object Profile : Screen("profile", "Profile", R.drawable.ic_person)
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Categories.route) { CategoriesScreen() }
        composable(Screen.Cart.route) { ShoppingCartScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }
    }
}