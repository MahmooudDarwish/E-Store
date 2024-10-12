package com.example.e_store.features.authentication.view

import android.util.Log
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
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.features.authentication.components.PasswordInputField
import com.example.e_store.features.authentication.components.PhoneNumberInputField
import com.example.e_store.features.authentication.components.WelcomeLabel
import com.example.e_store.features.authentication.view_model.AuthenticationViewModel
import com.example.e_store.utils.navigation.Screen
import com.example.e_store.utils.shared_components.AnimationImage
import com.example.e_store.utils.shared_components.EShopButton
import com.example.e_store.utils.shared_components.EShopLoadingIndicator
import com.example.e_store.utils.shared_components.EShopTextButton
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignUpScreen(navController: NavHostController, viewModel: AuthenticationViewModel) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        WelcomeLabel()

        if (viewModel.isProgressing.value) {
            EShopLoadingIndicator()
        }

        AnimationImage(
            imageRes = R.drawable.logo,
            initialSize = 150f,
            finalSize = 200f,
            animationDuration = 2000
        )

        OutlinedTextField(
            value = viewModel.name.value,
            onValueChange = {
                viewModel.onNameChanged(it)
            },
            label = { Text(text = stringResource(id = R.string.name_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        PhoneNumberInputField(
            value = viewModel.phone.value, onValueChange = {
                viewModel.onPhoneChanged(it)
            }, modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.email.value,
            onValueChange = {
                viewModel.onEmailChanged(it)
            },
            label = { Text(text = stringResource(id = R.string.email_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        PasswordInputField(
            value = viewModel.password.value, onValueChange = { newValue ->
                viewModel.onPasswordChanged(newValue)
            }, modifier = Modifier.fillMaxWidth()
        )

        PasswordInputField(
            value = viewModel.confirmPassword.value, onValueChange = { newValue ->
                viewModel.onConfirmPasswordChanged(newValue)

            }, modifier = Modifier.fillMaxWidth()
        )

        EShopButton(
            text = stringResource(id = R.string.sign_up),
            onClick = {
                Log.d("SignUpScreen", "Sign Up button clicked")
                viewModel.isProgressing.value = true
                viewModel.signUpUser(context, onAuthSuccess = {
                    viewModel.isProgressing.value = false
                    navController.navigate(Screen.SignIn.route)
                    Toast.makeText(
                        context,
                        getString(context, R.string.sign_up_successful),
                        Toast.LENGTH_SHORT
                    ).show()
                }, onError = {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    viewModel.isProgressing.value = false
                })

            },
            modifier = Modifier
                .fillMaxWidth(),
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(id = R.string.already_have_account))
            EShopTextButton(
                text = stringResource(id = R.string.sign_in),
                onClick = {
                    navController.navigate(Screen.SignIn.route)
                },
            )
        }
    }

}