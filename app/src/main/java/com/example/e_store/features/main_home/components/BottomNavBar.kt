package com.example.e_store.features.main_home.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.navigation.Screen

@Composable
fun BottomNavigationBar(
    items: List<Screen>,
    currentRoute: String,
    navController: NavHostController
) {
    NavigationBar(containerColor = Color.White, tonalElevation = 10.dp) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = if (currentRoute == item.route) PrimaryColor else Color.Gray
                    )
                },
                label = {
                    Text(item.title, color = PrimaryColor)
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
