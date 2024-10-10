package com.example.e_store.features.profile.components


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.utils.navigation.Screen
import com.example.e_store.utils.shared_components.ConfirmNegativeActionDialog
import com.example.e_store.utils.shared_components.Gap
import com.example.e_store.utils.shared_models.UserSession
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UserLayout(navHostController: NavHostController) {

    var showDialog by remember { mutableStateOf(false) }
    Text(
        text = stringResource(
            id = R.string.hello_user,
            UserSession.name ?: R.string.user
        ),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        textAlign = TextAlign.Center
    )

    Gap(height = 30)

    ProfileButton(
        text = stringResource(id = R.string.my_orders),
        icon = ImageVector.vectorResource(id = R.drawable.ic_orders),
        onClick = {
            navHostController.navigate(Screen.Orders.route)
        },
    )

    ProfileButton(
        text = stringResource(id = R.string.my_wishlist),
        icon = Icons.Default.FavoriteBorder,
        onClick = {
            ///TODO: Navigate to Wishlist screen @kk98989898 @MahmoudDarwish
            navHostController.navigate(Screen.FavouriteFromProfile.route)
        },
    )

    ProfileButton(
        text = stringResource(id = R.string.delivery_address),
        icon = Icons.Default.Place,
        onClick = {
            navHostController.navigate(Screen.Location.route)
        },
    )

    ProfileButton(
        text = stringResource(id = R.string.settings),
        icon = Icons.Default.Settings,
        onClick = {
            navHostController.navigate(Screen.Settings.route)
        },
    )

    ConfirmNegativeActionDialog(
        confirmText = "Log Out",
        showDialog = showDialog,
        title =stringResource(id = R.string.log_out) ,
        message = stringResource(R.string.are_you_sure_you_want_to_log_out),
        onConfirm = {
            FirebaseAuth.getInstance().signOut()
            navHostController.navigate(Screen.SignIn.route) {
                popUpTo(0) {
                    inclusive = true
                }
            }
        }) {
        showDialog = false
    }

    ProfileButton(
        text = stringResource(id = R.string.log_out),
        icon = ImageVector.vectorResource(id = R.drawable.ic_log_out),
        iconColor = Color.Red,
        textColor = Color.Red,
        onClick = {
            showDialog = true
        }
    )
}