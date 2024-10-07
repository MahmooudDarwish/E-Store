package com.example.e_store.features.main_home.view


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.e_store.R
import com.example.e_store.features.brand_products.view_model.BrandProductsViewModelFactory
import com.example.e_store.features.home.view_model.HomeViewModelFactory
import com.example.e_store.features.main_home.components.BottomNavigationBar
import com.example.e_store.features.profile.view_model.ProfileViewModelFactory
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.navigation.AppNavigation
import com.example.e_store.utils.navigation.Screen
import com.example.e_store.features.product_info.view_model.ProductInfoViewModelFactory
import com.example.e_store.features.shopping_cart.view_model.ShoppingCartViewModelFactory
import com.example.e_store.features.categories.view_model.CategoriesViewModelFactory
import com.example.e_store.features.orders.view_model.OrdersViewModelFactory
import com.example.e_store.features.search.view_model.SearchViewModelFactory
import com.example.e_store.utils.shared_components.NoInternetScreen
import com.example.weather.utils.managers.InternetChecker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHomeScreen(
    homeViewModelFactory: HomeViewModelFactory,
    brandProductsViewModelFactory: BrandProductsViewModelFactory,
    shoppingCartViewModelFactory: ShoppingCartViewModelFactory,
    productInfoViewModelFactory: ProductInfoViewModelFactory,
    searchViewModelFactory: SearchViewModelFactory,
    categoriesViewModelFactory: CategoriesViewModelFactory,
    profileViewModelFactory: ProfileViewModelFactory,
    ordersViewModelFactory : OrdersViewModelFactory
) {
    val items = listOf(
        Screen.Home,
        Screen.Categories,
        Screen.Cart,
        Screen.Profile
    )

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    // Use startsWith for checking and avoid multiple conditions
    val selectedRoute = when {
        currentRoute.startsWith(Screen.Home.route) -> Screen.Home.route
        currentRoute.startsWith(Screen.Categories.route) -> Screen.Categories.route
        currentRoute.startsWith(Screen.Cart.route) -> Screen.Cart.route
        currentRoute.startsWith(Screen.Profile.route) -> Screen.Profile.route
        else -> ""
    }

    val context = LocalContext.current
    val internetChecker = remember { InternetChecker(context) }
    var isInternetAvailable by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        internetChecker.startMonitoring()
        internetChecker.networkStateFlow.collect { isConnected ->
            isInternetAvailable = isConnected
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            internetChecker.stopMonitoring()
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            if (currentRoute in items.map { it.route }) {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
                    title = {
                        Text(
                            text = context.getString(R.string.app_name),
                            color = PrimaryColor,
                            fontStyle = FontStyle.Italic
                        )
                    }
                )
            }

        },
        bottomBar = {
            if (isInternetAvailable &&
                (currentRoute in items.map { it.route }
                        || currentRoute.startsWith(Screen.Home.route)
                        || currentRoute.startsWith(Screen.Categories.route)
                        || currentRoute.startsWith(Screen.Cart.route)
                        || currentRoute.startsWith(Screen.Profile.route))) {

                    BottomNavigationBar(
                        items = items,
                        currentRoute = selectedRoute,
                        navController = navController,
                    )

            }
        }
    ) { paddingValues ->
                if (isInternetAvailable) {
                    Box(modifier = Modifier.padding(paddingValues)) {
                        AppNavigation(
                            navController = navController,
                            homeViewModelFactory = homeViewModelFactory,
                            brandProductsViewModelFactory = brandProductsViewModelFactory,
                            categoriesViewModelFactory = categoriesViewModelFactory,
                            searchViewModelFactory = searchViewModelFactory,
                            profileViewModelFactory = profileViewModelFactory,
                            productInfoViewModelFactory = productInfoViewModelFactory,
                            shoppingCartViewModelFactory = shoppingCartViewModelFactory,
                            ordersViewModelFactory = ordersViewModelFactory,

                        )
                    }
                } else {
                    NoInternetScreen()
                }

        }


}