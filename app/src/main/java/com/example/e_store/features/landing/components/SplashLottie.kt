package com.example.e_store.features.landing.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.e_store.R
import com.example.e_store.utils.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashLottie(navController: NavController) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))

    LaunchedEffect(composition) {
        if (composition != null) {
            delay(3000)
            navController.navigate(Screen.Sign_in.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }


    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
}


