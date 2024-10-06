package com.example.e_store.features.profile.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.utils.navigation.Screen
import com.example.e_store.utils.shared_components.Gap

@Composable
fun GuestLayout(navHostController: NavHostController){
    Text(
        text = stringResource(id = R.string.hello_guest),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        textAlign = TextAlign.Center
    )
    Gap(height = 30)

    ProfileButton(
        text = stringResource(id = R.string.sign_in),
        icon = Icons.Default.AccountCircle,
        onClick = {
            navHostController.navigate(Screen.SignIn.route)
        },
    )

    ProfileButton(
        text = stringResource(id = R.string.sign_up),
        icon = Icons.Default.AddCircle,
        onClick = {
            navHostController.navigate(Screen.SignUp.route)
        },
    )
}