package com.example.e_store.features.authentication.view

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.utils.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import com.example.e_store.features.authentication.view_model.AuthenticationViewModel
import com.example.e_store.features.authentication.view_model.AuthenticationViewModelFactory
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver




@Composable
fun SignInScreen(navController: NavHostController) {

    val context = LocalContext.current
    val viewModel: AuthenticationViewModel =
        viewModel(factory = AuthenticationViewModelFactory(FirebaseAuth.getInstance()))

    val isProgressing = viewModel.isProgressing // Managed via ViewModel

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
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                contentDescription = "e-store logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(animatedImageSize)
                    .clip(CircleShape)
                    .rotate(animatedRotationAngle)
            )
        }

        OutlinedTextField(value = viewModel.email.value,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        PasswordInputField(
            value = viewModel.password.value,
            onValueChange = { viewModel.onPasswordChanged(it) },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                Log.d("Navigation", "Attempting to navigate to home")
                viewModel.signInAndCheckEmailVerification(
                    context,
                    onAuthSuccess = {
                        Log.d("Navigation", "Attempting to navigate to home")
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.SignIn.route) {
                                inclusive = true
                            }
                        }
                        Log.d("Navigation", "Navigation command executed")
                        viewModel.initializeUserSession(context, viewModel.email.value, false)
                        Log.d("Navigation", "User session initialized")
                    },
                    onError = { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            enabled = !viewModel.isProgressing.value,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (viewModel.isProgressing.value) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Sign In")
            }
        }

        TextButton(
            onClick = { navController.navigate(Screen.SignUp.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Don't have an account? Sign Up")
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
fun WelcomeLabel() {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                color = colorScheme.primaryContainer.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(
            text = "Welcome to e-store",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                letterSpacing = 0.5.sp
            ),
            color = colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}




