package com.example.e_store.features.authentication.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.features.authentication.components.PasswordInputField
import com.example.e_store.features.authentication.components.WelcomeLabel
import com.example.e_store.utils.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.example.e_store.features.authentication.view_model.AuthenticationViewModel
import com.example.e_store.features.authentication.view_model.AuthenticationViewModelFactory
import com.example.e_store.utils.shared_components.AnimationImage
import com.example.e_store.utils.shared_components.EShopButton
import com.example.e_store.utils.shared_components.EShopLoadingIndicator
import com.example.e_store.utils.shared_components.EShopTextButton


@Composable
fun SignInScreen(navController: NavHostController) {
    val viewModel: AuthenticationViewModel =
        viewModel(factory = AuthenticationViewModelFactory(FirebaseAuth.getInstance()))
    val isProgressing = viewModel.isProgressing
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WelcomeLabel()
        if (isProgressing.value) {
            EShopLoadingIndicator()
        }

        AnimationImage(
            imageRes = R.drawable.logo,
            initialSize = 150f,
            finalSize = 200f,
            animationDuration = 2000
        )

        OutlinedTextField(
            value = viewModel.email.value,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = { Text(stringResource(id = R.string.email_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        PasswordInputField(
            value = viewModel.password.value,
            onValueChange = { viewModel.onPasswordChanged(it) },
            modifier = Modifier.fillMaxWidth()
        )

        EShopButton(
            text = stringResource(id = R.string.sign_in),
            onClick = {
                viewModel.signInAndCheckEmailVerification(context, onAuthSuccess = {
                    navController.navigate(Screen.Home.route){
                        popUpTo(0){
                            inclusive = true
                        }
                    }
                    viewModel.initializeUserSession(context, viewModel.email.value, false)
                    Toast.makeText(
                        context,
                        getString(context, R.string.sign_in_successful),
                        Toast.LENGTH_SHORT
                    ).show()
                }, onError = {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                })
            },
            modifier = Modifier
                .fillMaxWidth(),
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(id = R.string.dont_have_account))
            EShopTextButton(
                text = stringResource(id = R.string.sign_up),
                onClick = {
                    navController.navigate(Screen.SignUp.route)
                })

        }

        EShopTextButton(
            text = stringResource(id = R.string.continue_as_guest),
            onClick = {
                viewModel.handleGuestModeSignIn(context)
                navController.navigate(Screen.Home.route)
                isProgressing.value = false
                Toast.makeText(
                    context,
                    getString(context, R.string.signed_in_as_guest),
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}



