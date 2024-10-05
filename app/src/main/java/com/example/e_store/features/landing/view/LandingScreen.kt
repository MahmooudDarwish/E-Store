package com.example.e_store.features.landing.view


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.e_store.features.home.view_model.HomeViewModelFactory
import com.example.e_store.features.landing.components.SplashLottie
import com.example.e_store.features.main_home.view.MainHomeScreen
import com.example.e_store.utils.data_layer.EStoreRepositoryImpl
import com.example.e_store.utils.data_layer.local.room.EStoreLocalDataSourceImpl
import com.example.e_store.utils.data_layer.remote.EStoreRemoteDataSourceImpl
import com.example.e_store.utils.navigation.Screen
import com.example.e_store.features.authentication.view.Sign_in_Screen
import com.example.e_store.features.authentication.view.Sign_up_Screen
import com.example.e_store.features.brand_products.view_model.BrandProductsViewModelFactory
import com.example.e_store.features.categories.view_model.CategoriesViewModel
import com.example.e_store.features.categories.view_model.CategoriesViewModelFactory

class LandingScreen : ComponentActivity() {

    private val repo by lazy {
        EStoreRepositoryImpl.getInstance(
            EStoreRemoteDataSourceImpl.getInstance(),
            EStoreLocalDataSourceImpl()
        )
    }

    private val homeViewModelFactory by lazy {
        HomeViewModelFactory(repo)
    }
    private val brandProductsViewModelFactory by lazy {
        BrandProductsViewModelFactory(repo)
    }
    private val categoriesViewModelFactory by lazy {
        CategoriesViewModelFactory(repo)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = Screen.Home.route) {
                        composable(Screen.Splash.route) { SplashLottie(navController) }
                        composable(Screen.Home.route) {
                            MainHomeScreen(
                                homeViewModelFactory = homeViewModelFactory,
                                brandProductsViewModelFactory = brandProductsViewModelFactory,
                                categoriesViewModelFactory = categoriesViewModelFactory
                            )
                        }
                        composable(Screen.Sign_up.route) { Sign_up_Screen(navController = navController) }
                        composable(Screen.Sign_in.route) { Sign_in_Screen(navController = navController) }

                    }
                }
            }
        }
    }
}


