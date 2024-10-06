package com.example.e_store.features.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.utils.shared_components.Gap
import com.example.e_store.utils.shared_models.UserSession

@Composable
fun UserLayout(navHostController: NavHostController) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Profile Avatar",
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
    )
    Gap(height = 10)

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
            ///TODO: Navigate to My Orders screen @MahmoudDarwish
        },
    )

    ProfileButton(
        text = stringResource(id = R.string.my_wishlist),
        icon = Icons.Default.FavoriteBorder,
        onClick = {
            ///TODO: Navigate to Wishlist screen @kk98989898 @MahmoudDarwish
        },
    )

    ProfileButton(
        text = stringResource(id = R.string.delivery_address),
        icon = Icons.Default.Place,
        onClick = {
            ///TODO: Navigate to Delivery Address screen @mohamed.abdelrahim @MahmoudDarwish
        },
    )

    ProfileButton(
        text = stringResource(id = R.string.settings),
        icon = Icons.Default.Settings,
        onClick = {
            ///TODO: Navigate to Settings screen @mohamed.abdelrahim @MahmoudDarwish
        },
    )

    ProfileButton(
        text = stringResource(id = R.string.log_out),
        icon = ImageVector.vectorResource(id = R.drawable.ic_log_out),
        iconColor = Color.Red,
        textColor = Color.Red,
        onClick = {
            ///TODO: Log out from Firebase @kk98989898 @MahmoudDarwish
        },
    )
}