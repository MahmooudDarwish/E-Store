package com.example.e_store.features.main_home.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults.containerColor
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.e_store.utils.navigation.Screen

@Composable
fun BottomNavigationBar(
    items: List<Screen>,
    currentRoute: String,
    navController: NavHostController
) {
    NavigationBar(containerColor = Color.White ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = if (currentRoute == item.route) Color(0xFF9c597d) else Color.Gray
                    )
                },
                label = {
                    Text(item.title, color = Color(0xFF9c597d))
                },
                alwaysShowLabel = false,
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}