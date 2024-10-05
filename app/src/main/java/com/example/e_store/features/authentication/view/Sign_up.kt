package com.example.e_store.features.authentication.view

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.features.authentication.view_model.AuthenticationViewModel
import com.example.e_store.features.authentication.view_model.AuthenticationViewModelFactory
import com.example.e_store.utils.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


@Composable
fun Sign_up_Screen(navController: NavHostController) {

    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val viewModel: AuthenticationViewModel =
        viewModel(factory = AuthenticationViewModelFactory(auth))
    val isProgressing = remember { mutableStateOf(false) }

    var rotationAngle by remember { mutableStateOf(0f) }
    var imageSize by remember { mutableStateOf(150.dp) }
    var isAnimating by remember { mutableStateOf(true) }

    val animatedRotationAngle by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
    )
    val animatedImageSize by animateDpAsState(
        targetValue = imageSize, animationSpec = tween(durationMillis = 2000), label = ""
    )

    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            rotationAngle = 360f
            imageSize = 200.dp
            delay(2000)
            rotationAngle = 0f
            imageSize = 150.dp
            delay(2000)

            isAnimating = false
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        WelcomeLabel()

        if (isProgressing.value) {
            CircularProgressIndicator()
        }

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), shape = CircleShape
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(animatedImageSize)
                    .clip(CircleShape)
                    .rotate(animatedRotationAngle)
            )
        }


        OutlinedTextField(value = viewModel.name.value, onValueChange = {
            viewModel.onNameChanged(it)
        }, label = { Text(text = "Name") }, modifier = Modifier.fillMaxWidth()
        )

        PhoneNumberInputField(
            value = viewModel.phone.value, onValueChange = {
                viewModel.onPhoneChanged(it)
            }, modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(value = viewModel.email.value, onValueChange = {
            viewModel.onEmailChanged(it)
        }, label = { Text(text = "Email") }, modifier = Modifier.fillMaxWidth()
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


        Button(
            onClick = {
                viewModel.signUpUser(context, onAuthSuccess = {

                    isProgressing.value = false
                    navController.navigate(Screen.Sign_in.route)
                    viewModel.saveNameToSharedPref(
                        context = context,
                        email = viewModel.email.value,
                        name = viewModel.name.value,
                        phone = viewModel.phone.value
                    )
                    Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                }, onError = {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    isProgressing.value = false
                })

            }, modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Sign Up")
        }

        Spacer(modifier = Modifier.height(4.dp))


        TextButton(
            onClick = { navController.navigate(Screen.Sign_in.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Already have an account? Sign in")
        }

        TextButton(
            onClick = {
                viewModel.handleGuestModeSignIn(context)
                navController.navigate(Screen.Home.route)
                isProgressing.value = false
                Toast.makeText(context, "Signed in as guest", Toast.LENGTH_SHORT).show()
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue as Guest")
        }
    }
}


@Composable
fun PasswordInputField(
    value: String, onValueChange: (String) -> Unit, modifier: Modifier
) {
    val passwordVisible = remember { mutableStateOf(false) }
    OutlinedTextField(value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = { Text(text = "Password") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image =
                if (passwordVisible.value) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24
            IconButton(onClick = {
                passwordVisible.value = !passwordVisible.value
            }) {
                Icon(
                    painter = painterResource(id = image),
                    contentDescription = if (passwordVisible.value) "Hide password" else "Show password"
                )
            }
        },
        modifier = modifier
    )
}


@Composable
fun PhoneNumberInputField(
    value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier
) {

    OutlinedTextField(value = value, onValueChange = {
        onValueChange(it)
    }, label = { Text(text = "Phone Number") }, keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Phone
    ), modifier = modifier
    )
}







