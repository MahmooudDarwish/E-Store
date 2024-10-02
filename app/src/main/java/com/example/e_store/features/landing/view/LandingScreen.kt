package com.example.e_store.features.landing.view


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.e_store.features.landing.components.SplashLottie
import com.example.e_store.features.main_home.view.MainHomeScreen
import com.example.e_store.utils.navigation.Screen

class LandingScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = Screen.Splash.route) {
                        composable(Screen.Splash.route) { SplashLottie(navController) }
                        composable(Screen.Home.route) { MainHomeScreen() }

                    }
                }
            }
        }
    }
}


