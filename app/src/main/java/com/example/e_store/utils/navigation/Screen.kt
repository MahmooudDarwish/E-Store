package com.example.e_store.utils.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.e_store.R
import com.example.e_store.features.brand_products.view.BrandProducts
import com.example.e_store.features.brand_products.view_model.BrandProductsViewModel
import com.example.e_store.features.brand_products.view_model.BrandProductsViewModelFactory
import com.example.e_store.features.categories.view.CategoriesScreen
import com.example.e_store.features.categories.view_model.CategoriesViewModel
import com.example.e_store.features.categories.view_model.CategoriesViewModelFactory
import com.example.e_store.features.home.view.HomeScreen
import com.example.e_store.features.home.view_model.HomeViewModel
import com.example.e_store.features.home.view_model.HomeViewModelFactory
import com.example.e_store.features.product_info.view.ProductInfoScreen
import com.example.e_store.features.profile.view.ProfileScreen
import com.example.e_store.features.profile.view_model.ProfileViewModel
import com.example.e_store.features.profile.view_model.ProfileViewModelFactory
import com.example.e_store.features.shopping_cart.view.ShoppingCartScreen
import com.example.e_store.utils.constants.NavigationKeys

sealed class Screen(val route: String, val title: Int, val icon: Int) {

    object Splash : Screen(NavigationKeys.SPLASH_ROUTE, R.string.splash_title, 0)
    object Home : Screen(NavigationKeys.HOME_ROUTE, R.string.home_title, R.drawable.ic_home)
    object BrandProducts : Screen(NavigationKeys.BRANDS_ROUTE, R.string.brand_products, 0) {
        fun createRoute(brand: String) = "$route/$brand"
    }

    object SignUp : Screen(NavigationKeys.SIGN_UP_ROUTE, R.string.sign_up, 0)
    object SignIn : Screen(NavigationKeys.SIGN_IN_ROUTE, R.string.sign_in, 0)
    object Categories :
        Screen(NavigationKeys.CATEGORIES_ROUTE, R.string.categories_title, R.drawable.ic_categories)

    object Cart :
        Screen(NavigationKeys.CART_ROUTE, R.string.cart_title, R.drawable.ic_shopping_cart)

    object Profile :
        Screen(NavigationKeys.PROFILE_ROUTE, R.string.profile_title, R.drawable.ic_person)

    object ProductInfoFromHome : Screen(NavigationKeys.PRODUCT_INFO_ROUTE, R.string.product_info, 0)
    object ProductInfoFromCategories :
        Screen(NavigationKeys.PRODUCT_INFO_CATEGORIES_ROUTE, R.string.product_info, 0)
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    homeViewModelFactory: HomeViewModelFactory,
    brandProductsViewModelFactory: BrandProductsViewModelFactory,
    categoriesViewModelFactory: CategoriesViewModelFactory,
    profileViewModelFactory: ProfileViewModelFactory
) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(factory = homeViewModelFactory)
            HomeScreen(viewModel, navController)
        }
        composable(route = Screen.Categories.route) {
            val viewModel: CategoriesViewModel = viewModel(factory = categoriesViewModelFactory)
            CategoriesScreen(viewModel = viewModel, navController = navController)
        }
        composable(route = Screen.Cart.route) { ShoppingCartScreen() }
        composable(route = Screen.Profile.route) {
            val viewModel: ProfileViewModel = viewModel(factory = profileViewModelFactory)
            ProfileScreen(viewModel = viewModel, navController = navController)
        }

        composable(
            route = "${NavigationKeys.BRANDS_ROUTE}/{${NavigationKeys.BRAND_ID}}",
            arguments = listOf(navArgument(NavigationKeys.BRAND_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val viewModel: BrandProductsViewModel =
                viewModel(factory = brandProductsViewModelFactory)
            val brand = backStackEntry.arguments?.getString(NavigationKeys.BRAND_ID)
            BrandProducts(brandID = brand, navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.ProductInfoFromHome.route) { ProductInfoScreen(navController) }
        composable(route = Screen.ProductInfoFromCategories.route) { ProductInfoScreen(navController) }

    }
}