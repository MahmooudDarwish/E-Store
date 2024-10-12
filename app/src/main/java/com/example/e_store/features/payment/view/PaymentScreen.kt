package com.example.e_store.features.payment.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.example.e_store.utils.shared_components.sharedHeader
import kotlinx.coroutines.delay

@Composable
fun PaymentScreen(navController: NavController, viewModel: PaymentViewModel) {
    var cardholderName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    var cardholderNameError by remember { mutableStateOf("") }
    var cardNumberError by remember { mutableStateOf("") }
    var monthError by remember { mutableStateOf("") }
    var yearError by remember { mutableStateOf("") }
    var cvvError by remember { mutableStateOf("") }

    var cardTypeImage by remember { mutableStateOf(R.drawable.initcardimage) }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showPopup by remember { mutableStateOf(false) }
    var popupMessage by remember { mutableStateOf("") }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.paymentscreen))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
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
        val nameRegex = "^[a-zA-Z ]+$".toRegex() // Allow letters and spaces
        return name.length in 10..20 && nameRegex.matches(name)
    }

    fun isValidMonth(inputMonth: String): Boolean {
        val monthInt = inputMonth.toIntOrNull() ?: return false
        return monthInt in 1..12
    }

    fun validateInputs() {
        // Clear all errors first
        cardholderNameError = ""
        cardNumberError = ""
        monthError = ""
        yearError = ""
        cvvError = ""

        if (!isValidCardholderName(cardholderName)) {
            cardholderNameError =
                "Cardholder name must be between 10 and 20 characters and contain only letters."
        }
        if (cardNumber.length != 16) {
            cardNumberError = "Card number must be 16 digits."
        }
        if (!isValidMonth(month)) {
            monthError = "Invalid month. Please enter a value between 01 and 12."
        }
        if (!isValidYear(year)) {
            yearError = "Invalid year. Must be >= 19."
        }
        if (cvv.length != 3) {
            cvvError = "CVV must be 3 digits."
        }
    }

    LaunchedEffect(showPopup) {
        if (showPopup) {
            delay(3000)
            isLoading = false
            popupMessage = "Thank you for your payment!"
            delay(4000)
            showPopup = false
            navController.navigate(NavigationKeys.HOME_ROUTE)
        }
    }

    if (showPopup) {
        PaymentSuccessPopup(
            message = if (isLoading) "Processing your payment..." else popupMessage,
            composition = if (isLoading) compositionLoading else compositionSuccess
        )
    }

    fun determineCardType(cardNumber: String) {
        cardTypeImage = when {
            cardNumber.startsWith("4") -> R.drawable.visa
            cardNumber.startsWith("51") || cardNumber.startsWith("52") || cardNumber.startsWith("53") || cardNumber.startsWith(
                "54"
            ) || cardNumber.startsWith("55") -> R.drawable.mastercard

            cardNumber.startsWith("34") || cardNumber.startsWith("37") -> R.drawable.amex
            cardNumber.startsWith("6011") || cardNumber.startsWith("65") -> R.drawable.discover
            cardNumber.startsWith("5078") || cardNumber.startsWith("6078") -> R.drawable.meeza
            else -> R.drawable.notsupportcard
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
            .verticalScroll(rememberScrollState()),
    ) {

        sharedHeader(
            navController = navController,
            headerText = " Payment"
        )


        Column(
            modifier = Modifier
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier.size(150.dp)
            )

            OutlinedTextField(
                value = cardholderName,
                onValueChange = {
                    if (it.length <= 20 && it.all { char -> char.isLetter() || char == ' ' }) {
                        cardholderName = it
                    }
                    if (isValidCardholderName(cardholderName)) {
                        cardholderNameError = ""
                    }
                },
                label = { Text("Cardholder Name") },
                isError = cardholderNameError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (cardholderNameError.isNotEmpty()) {
                Text(cardholderNameError, color = Color.Red, modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = cardNumber,
                onValueChange = {
                    if (it.length <= 16 && it.all { char -> char.isDigit() }) {
                        cardNumber = it
                        determineCardType(cardNumber)
                    }
                    if (cardNumber.length == 16) {
                        cardNumberError = ""
                    }
                },
                label = { Text("Card Number (16 digits)") },
                trailingIcon = {
                    Image(
                        painter = painterResource(id = cardTypeImage),
                        contentDescription = "Card Type",
                        modifier = Modifier.size(32.dp)
                    )
                },
                isError = cardNumberError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (cardNumberError.isNotEmpty()) {
                Text(cardNumberError, color = Color.Red, modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = month,
                    onValueChange = {
                        if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                            month = it
                        }
                        if (isValidMonth(month)) {
                            monthError = ""
                        }
                    },
                    label = { Text("Month (MM)") },
                    isError = monthError.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))

                OutlinedTextField(
                    value = year,
                    onValueChange = {
                        if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                            year = it
                        }
                        if (isValidYear(year)) {
                            yearError = ""
                        }
                    },
                    label = { Text("Year (YY)") },
                    isError = yearError.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                )
            }
            if (monthError.isNotEmpty() || yearError.isNotEmpty()) {
                Text(monthError + yearError, color = Color.Red, modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = cvv,
                onValueChange = {
                    if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                        cvv = it
                    }
                    if (cvv.length == 3) {
                        cvvError = ""
                    }
                },
                label = { Text("CVV (3 digits)") },
                isError = cvvError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (cvvError.isNotEmpty()) {
                Text(cvvError, color = Color.Red, modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(20.dp))

            EShopButton(
                onClick = {
                    validateInputs()
                    if (cardholderNameError.isEmpty() && cardNumberError.isEmpty() && monthError.isEmpty() && yearError.isEmpty() && cvvError.isEmpty()) {
                        popupMessage = "Processing your payment..."
                        message = popupMessage
                        isLoading = true
                        showPopup = true
                        viewModel.sendEmailAnddeleteDraftOrder()
                    } else {
                        message = "Please correct the errors before proceeding."
                    }
                },
                text = "Make Payment",
                modifier = Modifier.fillMaxWidth()
            )

            if (message.isNotEmpty()) {
                Text(
                    message,
                    color = if (message == "Please correct the errors before proceeding.") Color.Red else Color.Green,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
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
                    textAlign = TextAlign.Center,
                )

            }
        }
    }
}


