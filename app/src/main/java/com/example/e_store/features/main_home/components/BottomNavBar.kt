package com.example.e_store.features.main_home.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.getString
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.navigation.Screen
import com.example.e_store.utils.shared_components.Popup
import com.example.e_store.utils.shared_models.UserSession

@Composable
fun BottomNavigationBar(
    items: List<Screen>,
    currentRoute: String,
    navController: NavHostController
) {
    val context = LocalContext.current


    var showLoginDialog by remember { mutableStateOf(false) }

    NavigationBar(containerColor = Color.White) {
        Popup(
            showDialog = showLoginDialog,
            onDismiss = { showLoginDialog = false },
            title = stringResource(R.string.login_required),
            body = stringResource(R.string.you_need_to_login_to_use_this_feature),
            confirmButtonText = stringResource(R.string.sign_in),
            onConfirm = {
                navController.navigate(Screen.SignIn.route) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            },
            dismissButtonText = stringResource(R.string.cancel),
        )
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = getString(context, item.title),
                        tint = if (currentRoute == item.route) PrimaryColor else Color.Gray
                    )
                },
                label = {
                    Text(getString(context, item.title), color = PrimaryColor)
                },
                alwaysShowLabel = false,
                selected = currentRoute == item.route,
                onClick = {
                    if (item.route == Screen.Cart.route && UserSession.isGuest) {
                        showLoginDialog = true
                    } else {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                }
            )
        }
    }
}



