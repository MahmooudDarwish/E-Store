package com.example.e_store.features.main_home.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.e_store.features.home.view_model.HomeViewModelFactory
import com.example.e_store.features.main_home.components.BottomNavigationBar
import com.example.e_store.utils.navigation.AppNavigation
import com.example.e_store.utils.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHomeScreen(homeViewModelFactory: HomeViewModelFactory) {

    val items = listOf(
        Screen.Home,
        Screen.Categories,
        Screen.Cart,
        Screen.Profile
    )

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val bottomBarRoutes = items.map { it.route }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "E-Store", color = Color(0xFF9c597d), fontStyle = FontStyle.Italic ) }
            )
        },
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                BottomNavigationBar(
                    items = items,
                    currentRoute = currentRoute,
                    navController = navController
                )
            }
        }
    ) {paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AppNavigation(navController, homeViewModelFactory)
        }
    }
}


