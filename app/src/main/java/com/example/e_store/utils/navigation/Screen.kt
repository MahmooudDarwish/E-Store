package com.example.e_store.utils.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.e_store.R
import com.example.e_store.features.categories.view.CategoriesScreen
import com.example.e_store.features.home.view.HomeScreen
import com.example.e_store.features.home.view_model.HomeViewModel
import com.example.e_store.features.home.view_model.HomeViewModelFactory
import com.example.e_store.features.profile.view.ProfileScreen
import com.example.e_store.features.shopping_cart.view.ShoppingCartScreen
import com.example.e_store.utils.constants.NavigationKeys

sealed class Screen(val route: String, val title: Int, val icon: Int) {

    object Splash : Screen(NavigationKeys.SPLASH_ROUTE, R.string.splash_title, 0)
    object Home : Screen(NavigationKeys.HOME_ROUTE, R.string.home_title, R.drawable.ic_home)
    object Categories :
        Screen(NavigationKeys.CATEGORIES_ROUTE, R.string.categories_title, R.drawable.ic_categories)

    object Cart :
        Screen(NavigationKeys.CART_ROUTE, R.string.cart_title, R.drawable.ic_shopping_cart)

    object Profile :
        Screen(NavigationKeys.PROFILE_ROUTE, R.string.profile_title, R.drawable.ic_person)
}

@Composable
fun AppNavigation(navController: NavHostController, homeViewModelFactory: HomeViewModelFactory) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(factory = homeViewModelFactory)
            HomeScreen(viewModel)
        }
        composable(Screen.Categories.route) { CategoriesScreen() }
        composable(Screen.Cart.route) { ShoppingCartScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }
    }

}