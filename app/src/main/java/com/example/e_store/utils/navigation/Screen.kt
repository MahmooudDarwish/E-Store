package com.example.e_store.utils.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.e_store.R
import com.example.e_store.features.categories.view.CategoriesScreen
import com.example.e_store.features.home.view.HomeScreen
import com.example.e_store.features.home.view_model.HomeViewModel
import com.example.e_store.features.profile.view.ProfileScreen
import com.example.e_store.features.shopping_cart.view.ShoppingCartScreen
import com.example.e_store.utils.constants.NavigationKeys

sealed class Screen(val route: String, val title: String, val icon: Int) {
    object Splash : Screen(NavigationKeys.SPLASH_ROUTE, "Splash", 0)
    object Home : Screen(NavigationKeys.HOME_ROUTE, "Home", R.drawable.ic_home)
    object Categories :
        Screen(NavigationKeys.CATEGORIES_ROUTE, "Categories", R.drawable.ic_categories)

    object Cart : Screen(NavigationKeys.CART_ROUTE, "Cart", R.drawable.ic_shopping_cart)
    object Profile : Screen(NavigationKeys.PROFILE_ROUTE, "Profile", R.drawable.ic_person)
}
    @Composable
    fun AppNavigation(navController: NavHostController) {
        NavHost(navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.Categories.route) { CategoriesScreen() }
            composable(Screen.Cart.route) { ShoppingCartScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
        }

}