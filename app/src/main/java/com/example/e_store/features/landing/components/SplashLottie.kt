package com.example.e_store.features.landing.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.e_store.R
import com.example.e_store.features.authentication.view_model.AuthenticationViewModel
import com.example.e_store.features.authentication.view_model.AuthenticationViewModelFactory
import com.example.e_store.utils.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashLottie(navController: NavController) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))


    val context = LocalContext.current
    val viewModel: AuthenticationViewModel =
        viewModel(factory = AuthenticationViewModelFactory(FirebaseAuth.getInstance()))
    val user = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(composition) {
        if (composition != null) {
            delay(3000)
            if (user != null)
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                    user.email?.let { viewModel.initializeUserSession(context, it, false) }
                }
            else {
                navController.navigate(Screen.SignIn.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
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


