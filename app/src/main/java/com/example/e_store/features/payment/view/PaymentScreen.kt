package com.example.e_store.features.payment.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.e_store.R
import com.example.e_store.features.payment.view_model.PaymentViewModel
import com.example.e_store.utils.constants.NavigationKeys
import com.example.e_store.utils.shared_components.EShopButton
import kotlinx.coroutines.delay

@Composable
fun PaymentScreen(navController: NavController, viewModel: PaymentViewModel) {
    var cardholderName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var cardTypeImage by remember { mutableStateOf(R.drawable.initcardimage) } // Default to unsupported card image
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showPopup by remember { mutableStateOf(false) }
    var popupMessage by remember { mutableStateOf("") }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.paymentscreen))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever // Loop the animation infinitely
    )
    val compositionLoading by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.paymentprocessing))


    val compositionSuccess by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.paymentsuccessful))


    val minValidYear = 19

    fun isValidYear(inputYear: String): Boolean {
        val yearInt = inputYear.toIntOrNull() ?: return false
        return yearInt >= minValidYear
    }

    fun isValidCardholderName(name: String): Boolean {
        if (name.isBlank()) return false
       // val nameRegex = "^[a-zA-Z]+$".toRegex()
        val nameRegex = "^[a-zA-Z ]+$".toRegex() // Allow letters and spaces
        return name.length in 10..20 && nameRegex.matches(name)
    }

    fun isValidMonth(inputMonth: String): Boolean {
        val monthInt = inputMonth.toIntOrNull() ?: return false
        return monthInt in 1..12
    }
    LaunchedEffect(showPopup) {
        if (showPopup) {
            delay(3000) // Wait for 3 seconds while processing
            isLoading = false
            popupMessage = "Thank you for your payment!"
            delay(4000) // Show the success message for 2 seconds
            showPopup = false // Hide popup

            navController.navigate(NavigationKeys.HOME_ROUTE)
        }
    }

    if (showPopup) {
        PaymentSuccessPopup(
            message = if (isLoading) "Processing your payment..." else popupMessage,
            composition = if (isLoading) compositionLoading else compositionSuccess
        )
    }

    // Function to determine card type and update image
    fun determineCardType(cardNumber: String) {
        cardTypeImage = when {
            cardNumber.startsWith("4") -> R.drawable.visa // Visa
            cardNumber.startsWith("51") || cardNumber.startsWith("52") || cardNumber.startsWith("53") || cardNumber.startsWith(
                "54"
            ) || cardNumber.startsWith("55") -> R.drawable.mastercard // MasterCard
            cardNumber.startsWith("34") || cardNumber.startsWith("37") -> R.drawable.amex // American Express
            cardNumber.startsWith("6011") || cardNumber.startsWith("65") -> R.drawable.discover // Discover
            cardNumber.startsWith("5078") || cardNumber.startsWith("6078") -> R.drawable.meeza // Meeza
            else -> R.drawable.notsupportcard // Unsupported
        }

        if (cardTypeImage == R.drawable.notsupportcard) {
            message = "Unsupported card type! We support Visa, MasterCard, Amex, Discover, Meeza."
        } else {
            message = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Payment", style = MaterialTheme.typography.headlineMedium)

        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier
                .size(150.dp)
        )

        OutlinedTextField(
            value = cardholderName,
            onValueChange = {
                // Ensure only valid characters and limit length
                if (it.length <= 20 && it.all { char -> char.isLetter() || char == ' ' }) {
                    cardholderName = it
                }
            },
            label = { Text("Cardholder Name") },
            isError = !isValidCardholderName(cardholderName), // Show error if name is invalid
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = cardNumber,
            onValueChange = {
                if (it.length <= 16 && it.all { char -> char.isDigit() }) {
                    cardNumber = it
                    determineCardType(cardNumber)
                }
            },
            label = { Text("Card Number (16 digits)") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Image(
                    painter = painterResource(id = cardTypeImage),
                    contentDescription = "Card Type",
                    modifier = Modifier.size(32.dp) // Icon size
                )
            },
            isError = cardNumber.length != 14
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Month input field
            OutlinedTextField(
                value = month,
                onValueChange = {
                    // Ensure only digits and limit to 2 characters
                    if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                        month = it
                    }
                },
                label = { Text("Month (MM)") },
                isError = !isValidMonth(month), // Check if month is valid
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Year input field
            OutlinedTextField(
                value = year,
                onValueChange = {
                    // Ensure only digits and limit to 2 characters
                    if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                        year = it // Set year
                    }
                },
                label = { Text("Year (YY)") },
                isError = year.length != 2 || !isValidYear(year), // Show error if year is invalid
                modifier = Modifier.weight(1f)
            )
        }


        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = cvv,
            onValueChange = {
                // Ensure only digits and limit to 3 characters
                if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                    cvv = it
                }
            },
            label = { Text("CVV (3 digits)") },
            modifier = Modifier.fillMaxWidth(),
            isError = cvv.length != 3
        )

        Spacer(modifier = Modifier.height(20.dp))

        EShopButton(
            onClick = {
                // Simulate payment processing
                if (isValidCardholderName(cardholderName) && cardNumber.isNotBlank() && isValidMonth(
                        month
                    ) && isValidYear(year) && cvv.isNotBlank()
                ) {
                    popupMessage = "Processing your payment..."

                    message = popupMessage
                    isLoading = true
                    showPopup = true

                    viewModel.sendEmailAnddeleteDraftOrder()


                } else {
                    message = "Please fill all fields correctly!"
                }
            },
            text = "Pay Now"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = message,
            color = if (message.contains("Unsupported")) Color.Red else Color.Black
        )
    }
}


@Composable
fun PaymentSuccessPopup(message: String, composition: LottieComposition? = null) {
    Dialog(onDismissRequest = {}) {
        Surface(

            shape = MaterialTheme.shapes.medium,

            tonalElevation = 8.dp, // Add elevation
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),

        ) {
            Column(

                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .size(300.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign =  TextAlign.Center,
                )

              }
        }
    }
}


