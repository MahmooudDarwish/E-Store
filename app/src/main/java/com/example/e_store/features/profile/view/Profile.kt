package com.example.e_store.features.profile.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.e_store.features.profile.components.GuestLayout
import com.example.e_store.features.profile.components.UserLayout
import com.example.e_store.features.profile.view_model.ProfileViewModel
import com.example.e_store.utils.shared_models.UserSession

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (UserSession.isGuest) {
            GuestLayout(navHostController = navController)
        } else {
            UserLayout(navHostController = navController)
        }
    }
}

