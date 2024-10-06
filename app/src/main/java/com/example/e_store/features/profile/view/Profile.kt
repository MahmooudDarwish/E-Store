package com.example.e_store.features.profile.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.features.profile.components.GuestLayout
import com.example.e_store.features.profile.components.UserLayout
import com.example.e_store.features.profile.view_model.ProfileViewModel
import com.example.e_store.utils.shared_components.AnimationImage
import com.example.e_store.utils.shared_components.Gap
import com.example.e_store.utils.shared_models.UserSession

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, navController: NavHostController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimationImage(
            imageRes = R.drawable.logo,
            initialSize = 150f,
            finalSize = 200f,
            animationDuration = 2000
        )

        Gap(height = 10)
        if (UserSession.isGuest) {
            GuestLayout(navHostController = navController)
        } else {
            UserLayout(navHostController = navController)
        }
    }
}
