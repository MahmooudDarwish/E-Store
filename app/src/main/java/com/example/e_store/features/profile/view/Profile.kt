package com.example.e_store.features.profile.view

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.features.profile.components.GuestLayout
import com.example.e_store.features.profile.components.UserLayout
import com.example.e_store.features.profile.view_model.ProfileViewModel
import com.example.e_store.utils.shared_components.Gap
import com.example.e_store.utils.shared_models.UserSession
import kotlinx.coroutines.delay

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, navController: NavHostController) {

    var rotationAngle by remember { mutableFloatStateOf(0f) }
    var imageSize by remember { mutableStateOf(150.dp) }
    val animatedRotationAngle by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(durationMillis = 2000, easing = LinearEasing), label = ""

    )
    var isAnimating by remember { mutableStateOf(true) }

    val animatedImageSize by animateDpAsState(
        targetValue = imageSize, animationSpec = tween(durationMillis = 2000), label = ""
    )
    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            rotationAngle = 360f
            imageSize = 200.dp
            delay(2000)
            rotationAngle = 0f
            imageSize = 150.dp
            delay(2000)
            isAnimating = false
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), shape = CircleShape
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "e-store logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(animatedImageSize)
                    .clip(CircleShape)
                    .rotate(animatedRotationAngle)
            )
        }
        Gap(height = 10)
        if (UserSession.isGuest) {
            GuestLayout(navHostController = navController)
        } else {
            UserLayout(navHostController = navController)
        }
    }
}

