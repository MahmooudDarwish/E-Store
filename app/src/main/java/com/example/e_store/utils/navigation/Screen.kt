package com.example.e_store.utils.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.e_store.R
import com.example.e_store.features.authentication.view.SignInScreen
import com.example.e_store.features.authentication.view.SignUpScreen
import com.example.e_store.features.authentication.view_model.AuthenticationViewModel
import com.example.e_store.features.authentication.view_model.AuthenticationViewModelFactory
import com.example.e_store.features.brand_products.view.BrandProducts
import com.example.e_store.features.brand_products.view_model.BrandProductsViewModel
import com.example.e_store.features.brand_products.view_model.BrandProductsViewModelFactory
import com.example.e_store.features.categories.view.CategoriesScreen
import com.example.e_store.features.home.view.HomeScreen
import com.example.e_store.features.home.view_model.HomeViewModel
import com.example.e_store.features.home.view_model.HomeViewModelFactory
import com.example.e_store.features.orders.view.OrdersScreen
import com.example.e_store.features.orders.view_model.OrdersViewModel
import com.example.e_store.features.orders.view_model.OrdersViewModelFactory
import com.example.e_store.features.product_info.view.ProductInfoScreen
import com.example.e_store.features.profile.view.ProfileScreen
import com.example.e_store.features.shopping_cart.view.ShoppingCartScreen
import com.example.e_store.features.search.view.SearchScreen
import com.example.e_store.features.search.view_model.SearchViewModel
import com.example.e_store.features.search.view_model.SearchViewModelFactory
import com.example.e_store.utils.constants.NavigationKeys
import com.example.e_store.features.product_info.view_model.ProductInfoViewModel
import com.example.e_store.features.product_info.view_model.ProductInfoViewModelFactory
import com.example.e_store.features.shopping_cart.view_model.ShoppingCartViewModel
import com.example.e_store.features.shopping_cart.view_model.ShoppingCartViewModelFactory
import com.example.e_store.features.categories.view_model.CategoriesViewModel
import com.example.e_store.features.categories.view_model.CategoriesViewModelFactory
import com.example.e_store.features.checkout.view.CheckoutScreen
import com.example.e_store.features.checkout.viewModel.CheckoutViewModel
import com.example.e_store.features.checkout.viewModel.CheckoutViewModelFactory
import com.example.e_store.features.favourites.view.FavouritesScreen
import com.example.e_store.features.favourites.view_model.FavouritesViewModel
import com.example.e_store.features.favourites.view_model.FavouritesViewModelFactory
import com.example.e_store.features.location.view.AddLocationScreen
import com.example.e_store.features.location.view.MapScreen
import com.example.e_store.features.location.view.LocationScreen
import com.example.e_store.features.location.view_model.AddLocationViewModel
import com.example.e_store.features.location.view_model.AddLocationViewModelFactory
import com.example.e_store.features.location.view_model.LocationViewModel
import com.example.e_store.features.location.view_model.LocationViewModelFactory
import com.example.e_store.features.location.view_model.MapViewModel
import com.example.e_store.features.location.view_model.MapViewModelFactory
import com.example.e_store.features.payment.view.PaymentScreen
import com.example.e_store.features.payment.view_model.PaymentViewModel
import com.example.e_store.features.payment.view_model.PaymentViewModelFactory
import com.example.e_store.features.profile.view_model.ProfileViewModel
import com.example.e_store.features.profile.view_model.ProfileViewModelFactory
import com.example.e_store.features.settings.view.SettingsScreen
import com.example.e_store.features.settings.view_model.SettingsViewModel
import com.example.e_store.features.settings.view_model.SettingsViewModelFactory

sealed class Screen(val route: String, val title: Int, val icon: Int) {

    object Checkout : Screen(NavigationKeys.CHECKOUT_ROUTE, R.string.checkout, 0)

    object  Map : Screen(NavigationKeys.MAP_ROUTE, R.string.map, 0)
    object  Location : Screen(NavigationKeys.LOCATION_ROUTE, R.string.location, 0)
    object  Payment : Screen(NavigationKeys.PAYMENT_ROUTE, R.string.payment, 0)

    object Settings : Screen(NavigationKeys.SETTINGS_ROUTE, R.string.settings,0)

    object Splash : Screen(NavigationKeys.SPLASH_ROUTE, R.string.splash_title, 0)
    object Home : Screen(NavigationKeys.HOME_ROUTE, R.string.home_title, R.drawable.ic_home)
    object BrandProducts : Screen(NavigationKeys.BRANDS_ROUTE, R.string.brand_products, 0) {
        fun createRoute(brand: String) = "$route/$brand"
    }

    object Orders : Screen(NavigationKeys.ORDERS_ROUTE, R.string.orders_title, 0)
    object SignUp : Screen(NavigationKeys.SIGN_UP_ROUTE, R.string.sign_up, 0)
    object SignIn : Screen(NavigationKeys.SIGN_IN_ROUTE, R.string.sign_in, 0)

    object Categories :
        Screen(NavigationKeys.CATEGORIES_ROUTE, R.string.categories_title, R.drawable.ic_categories)

    object Cart :
        Screen(NavigationKeys.CART_ROUTE, R.string.cart_title, R.drawable.ic_shopping_cart)

    object Profile :
        Screen(NavigationKeys.PROFILE_ROUTE, R.string.profile_title, R.drawable.ic_person)

    object ProductInfoFromCategories :
        Screen(NavigationKeys.PRODUCT_INFO_CATEGORIES_ROUTE, R.string.product_info, 0)

    object ProductInfoFromHome :
        Screen(NavigationKeys.PRODUCT_INFO_HOME_ROUTE, R.string.product_info, 0)

    object SearchFromHome : Screen(NavigationKeys.SEARCH_HOME_ROUTE, R.string.search, 0)
    object SearchFromCategories :
        Screen(NavigationKeys.SEARCH_CATEGORIES_ROUTE, R.string.search, 0)

    object FavouriteFromHome :
        Screen(NavigationKeys.FAVOURITE_ROUTE_FROM_HOME, R.string.favourite, 0)
    object FavouriteFromCategories :
        Screen(NavigationKeys.FAVOURITE_ROUTE_FROM_CATEGORIES, R.string.favourite, 0)
    object FavouriteFromProfile :
        Screen(NavigationKeys.FAVOURITE_ROUTE_FROM_PROFILE, R.string.favourite, 0)
    object ProductInfoFromFavourite : Screen(NavigationKeys.PRODUCT_INFO_FAVOURITE_ROUTE, R.string.product_info, 0)
    object ProductInfoFromProfile : Screen(NavigationKeys.PRODUCT_INFO_PROFILE_ROUTE, R.string.product_info, 0)

    object AddLocation : Screen(NavigationKeys.ADD_LOCATION_ROUTE, R.string.add_location, 0)

}

@Composable
fun AppNavigation(
    navController: NavHostController,
    homeViewModelFactory: HomeViewModelFactory,
    brandProductsViewModelFactory: BrandProductsViewModelFactory,
    searchViewModelFactory: SearchViewModelFactory,
    categoriesViewModelFactory: CategoriesViewModelFactory,
    productInfoViewModelFactory: ProductInfoViewModelFactory,
    shoppingCartViewModelFactory: ShoppingCartViewModelFactory,
    profileViewModelFactory: ProfileViewModelFactory,
    favouritesViewModelFactory: FavouritesViewModelFactory,
    ordersViewModelFactory: OrdersViewModelFactory,
    settingsViewModelFactory: SettingsViewModelFactory,

    authenticationViewModelFactory : AuthenticationViewModelFactory,
    checkoutViewModelFactory: CheckoutViewModelFactory,
    addLocationViewModelFactory: AddLocationViewModelFactory,
    mapViewModelFactory: MapViewModelFactory,
    locationViewModelFactory: LocationViewModelFactory,
    paymentViewModelFactory : PaymentViewModelFactory,

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
        composable(route = Screen.Profile.route) {
            val viewModel: ProfileViewModel = viewModel(factory = profileViewModelFactory)
            ProfileScreen(viewModel = viewModel, navController = navController)
        }

        composable(route = Screen.Checkout.route) {
            val viewModel: CheckoutViewModel = viewModel(factory = checkoutViewModelFactory)
            CheckoutScreen(viewModel = viewModel, navController = navController)
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

        composable(route = Screen.ProductInfoFromHome.route) {
            val viewModel: ProductInfoViewModel = viewModel(factory = productInfoViewModelFactory)

            ProductInfoScreen(navController, viewModel)
        }
        composable(route = Screen.ProductInfoFromCategories.route) {
            val viewModel: ProductInfoViewModel = viewModel(factory = productInfoViewModelFactory)
            ProductInfoScreen(navController, viewModel)
        }

        composable(route = Screen.ProductInfoFromFavourite.route) {
            val viewModel: ProductInfoViewModel = viewModel(factory = productInfoViewModelFactory)
            ProductInfoScreen(navController, viewModel)
        }
        composable(route = Screen.ProductInfoFromProfile.route) {
            val viewModel: ProductInfoViewModel = viewModel(factory = productInfoViewModelFactory)
            ProductInfoScreen(navController, viewModel)
        }


        composable(route = Screen.SearchFromHome.route) {
            val viewModel: SearchViewModel = viewModel(factory = searchViewModelFactory)
            SearchScreen(viewModel, navController)
        }
        composable(route = Screen.SearchFromCategories.route) {
            val viewModel: SearchViewModel = viewModel(factory = searchViewModelFactory)
            SearchScreen(viewModel, navController)
        }
        composable(route = Screen.Orders.route) {
            val viewModel: OrdersViewModel = viewModel(factory = ordersViewModelFactory)
            OrdersScreen(viewModel = viewModel, navController = navController)
        }




        composable(route = Screen.SignIn.route) {
            val viewModel: AuthenticationViewModel = viewModel(factory = authenticationViewModelFactory)
            SignInScreen(navController, viewModel) }
        composable(route = Screen.SignUp.route) {
            val viewModel: AuthenticationViewModel = viewModel(factory = authenticationViewModelFactory)
            SignUpScreen(navController,viewModel) }

        composable(route = Screen.Cart.route) {
            val viewModel: ShoppingCartViewModel = viewModel(factory = shoppingCartViewModelFactory)
            ShoppingCartScreen(viewModel, navController)
        }
        composable(route = Screen.FavouriteFromHome.route) {
            val viewModel: FavouritesViewModel = viewModel(factory = favouritesViewModelFactory)
            FavouritesScreen(viewModel, navController)
        }
        composable(route = Screen.FavouriteFromCategories.route) {
            val viewModel: FavouritesViewModel = viewModel(factory = favouritesViewModelFactory)
            FavouritesScreen(viewModel, navController)
        }
        composable(route = Screen.FavouriteFromProfile.route) {
            val viewModel: FavouritesViewModel = viewModel(factory = favouritesViewModelFactory)
            FavouritesScreen(viewModel, navController)
        }

        composable(route = Screen.AddLocation.route) {
            val viewModel: AddLocationViewModel = viewModel(factory = addLocationViewModelFactory)
            AddLocationScreen(navController, viewModel)
        }
        composable(route = Screen.Map.route) {
            val viewModel: MapViewModel = viewModel(factory = mapViewModelFactory)
            MapScreen(navController, viewModel)
        }
        composable(route = Screen.Location.route) {
            val viewModel: LocationViewModel = viewModel(factory = locationViewModelFactory)
            LocationScreen(navController, viewModel)
        }
        composable(route = Screen.Payment.route) {

            val viewModel: PaymentViewModel = viewModel(factory = paymentViewModelFactory)
            PaymentScreen(navController,viewModel)
        }
        composable(route = Screen.Settings.route) {
            val viewModel: SettingsViewModel = viewModel(factory = settingsViewModelFactory)
            SettingsScreen(viewModel, navController) }
    }
}
