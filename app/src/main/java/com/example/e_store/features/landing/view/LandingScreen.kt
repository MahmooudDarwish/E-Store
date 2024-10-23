package com.example.e_store.features.landing.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.e_store.features.authentication.view.SignInScreen
import com.example.e_store.features.authentication.view.SignUpScreen
import com.example.e_store.features.authentication.view_model.AuthenticationViewModel
import com.example.e_store.features.authentication.view_model.AuthenticationViewModelFactory
import com.example.e_store.features.brand_products.view_model.BrandProductsViewModelFactory
import com.example.e_store.features.categories.view_model.CategoriesViewModelFactory
import com.example.e_store.features.checkout.viewModel.CheckoutViewModelFactory
import com.example.e_store.features.favourites.view_model.FavouritesViewModelFactory
import com.example.e_store.features.home.view_model.HomeViewModelFactory
import com.example.e_store.features.landing.components.SplashLottie
import com.example.e_store.features.location.view_model.AddLocationViewModelFactory
import com.example.e_store.features.location.view_model.LocationViewModelFactory
import com.example.e_store.features.location.view_model.MapViewModelFactory
import com.example.e_store.features.main_home.view.MainHomeScreen
import com.example.e_store.features.orders.view_model.OrdersViewModelFactory
import com.example.e_store.features.payment.view_model.PaymentViewModelFactory
import com.example.e_store.features.product_info.view_model.ProductInfoViewModelFactory
import com.example.e_store.features.profile.view_model.ProfileViewModelFactory
import com.example.e_store.features.search.view_model.SearchViewModelFactory
import com.example.e_store.features.settings.view_model.SettingsViewModelFactory
import com.example.e_store.features.shopping_cart.view_model.ShoppingCartViewModelFactory
import com.example.e_store.utils.data_layer.EStoreRepositoryImpl
import com.example.e_store.utils.data_layer.remote.EStoreRemoteDataSourceImpl
import com.example.e_store.utils.navigation.Screen
import com.example.e_store.utils.shared_view_model.FavouriteControllerViewModel
import com.example.e_store.utils.shared_view_model.FavouriteControllerViewModelFactory
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LandingScreen : ComponentActivity() {

    private val repo by lazy {
        EStoreRepositoryImpl.getInstance(
            EStoreRemoteDataSourceImpl.getInstance(),
        )
    }


    private val homeViewModelFactory by lazy { HomeViewModelFactory(repo) }
    private val brandProductsViewModelFactory by lazy { BrandProductsViewModelFactory(repo) }
    private val searchViewModelFactory by lazy { SearchViewModelFactory(repo) }
    private val categoriesViewModelFactory by lazy { CategoriesViewModelFactory(repo) }
    private val profileViewModelFactory by lazy { ProfileViewModelFactory(repo) }
    private val productInfoViewModelFactory by lazy { ProductInfoViewModelFactory(repo) }
    private val shoppingCartViewModelFactory by lazy { ShoppingCartViewModelFactory(repo) }
    private val favouritesViewModelFactory by lazy { FavouritesViewModelFactory(repo) }
    private val ordersViewModelFactory by lazy { OrdersViewModelFactory(repo) }
    private val authenticationViewModelFactory by lazy {
        AuthenticationViewModelFactory(
            FirebaseAuth.getInstance(),
            repo
        )
    }
    private val checkoutViewModelFactory by lazy { CheckoutViewModelFactory(repo) }

    private val mapViewModelFactory by lazy { MapViewModelFactory(repo) }
    private val addLocationFactory by lazy { AddLocationViewModelFactory(repo) }
    private val locationViewModelFactory by lazy { LocationViewModelFactory(repo) }
    private val paymentViewModelFactory by lazy { PaymentViewModelFactory(repo) }
    private val settingsViewModelFactory by lazy { SettingsViewModelFactory(repo) }
    private val favouriteControllerViewModelFactory by lazy { FavouriteControllerViewModelFactory(repo) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            retryFirestoreWithExponentialBackoff(this@LandingScreen)
        }
        setContent {
            MaterialTheme {
                Surface {


                    val navController = rememberNavController()
                    NavHost(navController, startDestination = Screen.Splash.route) {
                        val viewModel: AuthenticationViewModel by viewModels { authenticationViewModelFactory }

                        composable(Screen.Splash.route) { SplashLottie(navController, viewModel) }
                        composable(Screen.Home.route) {
                            MainHomeScreen(
                                homeViewModelFactory = homeViewModelFactory,
                                brandProductsViewModelFactory = brandProductsViewModelFactory,
                                categoriesViewModelFactory = categoriesViewModelFactory,
                                searchViewModelFactory = searchViewModelFactory,
                                profileViewModelFactory = profileViewModelFactory,
                                productInfoViewModelFactory = productInfoViewModelFactory,
                                shoppingCartViewModelFactory = shoppingCartViewModelFactory,
                                favouritesViewModelFactory = favouritesViewModelFactory,
                                ordersViewModelFactory = ordersViewModelFactory,
                                authenticationViewModelFactory = authenticationViewModelFactory,
                                checkoutViewModelFactory = checkoutViewModelFactory,
                                mapViewModelFactory = mapViewModelFactory,
                                addLocationFactory = addLocationFactory,
                                locationViewModelFactory = locationViewModelFactory,
                                paymentViewModelFactory = paymentViewModelFactory,
                                settingsViewModelFactory = settingsViewModelFactory,
                                favouriteControllerViewModelFactory = favouriteControllerViewModelFactory
                            )
                        }
                        composable(Screen.SignUp.route) {
                            SignUpScreen(
                                navController = navController,
                                viewModel
                            )
                        }
                        composable(Screen.SignIn.route) {
                            SignInScreen(
                                navController = navController,
                                viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

@Suppress("DEPRECATION")
private fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}


suspend fun retryFirestoreWithExponentialBackoff(context: Context, retries: Int = 2, onFailure: (() -> Unit)? = null) {
    val delayTime = 1000L * retries  // Increase delay with each retry
    delay(delayTime)

    Log.d("LandingScreen", "Retrying Firestore request, attempt: $retries")

    if (isNetworkAvailable(context)) {
        try {
            Log.d("Firestore Init", "Initializing Firestore")
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
            }
        } catch (e: Exception) {
            if (retries < 5) {  // Limit retries to 5 attempts
                Log.e("Firestore Init", "Error initializing Firestore: ${e.message}")
                retryFirestoreWithExponentialBackoff(context, retries + 1, onFailure)
            } else {
                Log.e("Firestore Init", "Max retries reached. Could not initialize Firestore.")
                onFailure?.invoke()  // Notify the UI on failure
            }
        }
    } else {
        Log.e("Firestore Init", "No network available.")
        onFailure?.invoke()  // Notify the UI if there's no network
    }
}

