package com.example.e_store.features.checkout.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.e_store.R
import com.example.e_store.features.checkout.viewModel.CheckoutViewModel
import com.example.e_store.features.home.component.SliderItem
import com.example.e_store.features.home.component.updateSliderImages
import com.example.e_store.features.payment.view.PaymentSuccessPopup
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.constants.NavigationKeys
import com.example.e_store.utils.shared_components.EShopButton
import com.example.e_store.utils.shared_components.EShopLoadingIndicator
import com.example.e_store.utils.shared_components.ElevationCard
import com.example.e_store.utils.shared_methods.convertCurrency
import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.AddressResponse
import com.example.e_store.utils.shared_models.Customer
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DraftOrderDetails
import com.example.e_store.utils.shared_models.DraftOrderIDHolder
import kotlinx.coroutines.delay

@Composable
fun CheckoutScreen(viewModel: CheckoutViewModel, navController: NavHostController) {

    val context = LocalContext.current
    val customerDraftOrderState by viewModel.draftOrder.collectAsStateWithLifecycle()
    var orderDetails = remember { mutableStateOf<DraftOrderDetails?>(null) }
    var couponCode by remember { mutableStateOf(TextFieldValue("")) }
    var selectedPaymentMethod by remember { mutableStateOf("Cash on delivery") }

    val draftAddress by viewModel.defaultAddress.collectAsStateWithLifecycle()
    var addressDetails = remember { mutableStateOf<Address?>(null) }

    val compositionSuccess by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.paymentsuccessful))
    var showPopup by remember { mutableStateOf(false) }


    if (showPopup) {
        PaymentSuccessPopup(
            composition = compositionSuccess,
            message = "Thank you!"
        )
    }
    // Fetch draft order when the screen loads
    LaunchedEffect(Unit) {
        if (DraftOrderIDHolder.draftOrderId != null) {
            viewModel.fetchDraftOrderByID(DraftOrderIDHolder.draftOrderId!!)
            viewModel.fetchDefaultAddress()
        }

    }

    LaunchedEffect(showPopup) {
        if (showPopup) {
            delay(4000) // Dismiss after 4 seconds
            showPopup = false
            navController.navigate(NavigationKeys.HOME_ROUTE)
        }
    }


    // Handle different states of draftOrderState
    when (draftAddress) {
        is DataState.Loading -> {
            EShopLoadingIndicator()
        }

        is DataState.Success -> {
            val draftOrder = (draftAddress as DataState.Success<Address?>).data
            draftOrder?.let {
                addressDetails.value =
                    it // Directly update the value without re-creating mutableStateOf
                Log.d("CheckoutScreen", "Order Details: $it")


            }
        }

        is DataState.Error -> {
            val errorMsg = (draftAddress as DataState.Error).message
            LaunchedEffect(Unit) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            }
        }

        else -> {
            // Handle any unknown states or do nothing
        }
    }


    // Handle different states of draftOrderState
    when (customerDraftOrderState) {
        is DataState.Loading -> {
            EShopLoadingIndicator()
        }

        is DataState.Success -> {
            val draftOrder = (customerDraftOrderState as DataState.Success<DraftOrderDetails?>).data
            draftOrder?.let {
                orderDetails.value =
                    it // Directly update the value without re-creating mutableStateOf
                Log.d("CheckoutScreen", "Order Details: $it")


            }
        }

        is DataState.Error -> {
            val errorMsg = (customerDraftOrderState as DataState.Error).message
            LaunchedEffect(Unit) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            }
        }

        else -> {
            // Handle any unknown states or do nothing
        }
    }

    orderDetails.value?.let { details ->

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Check Out") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { contentPadding ->

            // Apply contentPadding to the Column
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(contentPadding)
                    .padding(16.dp, 30.dp, 16.dp, 16.dp), // Additional padding for the content
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    // Address Section

                    ElevationCard {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .background(Color.White)
                        )
                        {
                            Text(
                                text = "Choose Your Delivery Address",
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = addressDetails.value?.address1 ?: "",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center

                            ) {
                                /*        TextButton(onClick = { *//* Handle manual address edit *//* }) {
                                    Text(
                                        text = "Update Current Address",
                                        fontSize = 18.sp,
                                        color = PrimaryColor
                                    )
                                }*/
                                TextButton(onClick = {
                                    navController.navigate(NavigationKeys.LOCATION_ROUTE)
                                }) {
                                    Text(
                                        text = "Choose Another Address",
                                        fontSize = 18.sp,
                                        color = PrimaryColor
                                    )
                                }

                            }

                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // Coupon Code Section
                    TextField(
                        value = couponCode,
                        onValueChange = { couponCode = it },
                        placeholder = { Text("Enter Coupon Code") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Button(
                                modifier = Modifier.padding(end = 16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = colorResource(id = R.color.colorPrimaryButton),
                                    contentColor = Color.White
                                ),
                                shape = MaterialTheme.shapes.small,

                                onClick = {
                                    viewModel.applyDiscount(couponCode.text)
                                    Log.d("CheckotextutScreen", "Coupon Code: ${couponCode.text}")


                                }) {
                                Text(text = "Apply")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    // Order Summary
                    ElevationCard(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Order Summary",
                                fontSize = 18.sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Items", fontSize = 16.sp)
                                Text(
                                    text = details.line_items.size.toString(),
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Subtotal", fontSize = 16.sp)
                                Text(
                                    text = "${ details.subtotal_price?.let { convertCurrency(it.toDouble()) } }",
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Discount", fontSize = 16.sp)
                                Text(
                                    text = convertCurrency(details.applied_discount?.value?.toDouble() ?: 0.0 ),
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Total Tax", fontSize = 16.sp)
                                Text(
                                    text = convertCurrency(details.total_tax?.toDouble() ?: 0.0),
                                    fontSize = 16.sp
                                )
                            }

                            Divider(modifier = Modifier.padding(vertical = 8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Total",
                                    fontSize = 18.sp,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                )
                                Text(
                                    text = details.total_price?.let { convertCurrency(it.toDouble()) } ?: "N/A",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )

                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                    ElevationCard(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp) // Padding inside the card
                        ) {
                            // Payment Method Section
                            Text(
                                text = "Choose Payment Method",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp) // Spacing below the title
                            )

                            // Row for Cash on Delivery option
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp), // Spacing between rows
                                verticalAlignment = Alignment.CenterVertically // Align vertically in the center
                            ) {
                                RadioButton(
                                    selected = selectedPaymentMethod == "Cash on delivery",
                                    onClick = { selectedPaymentMethod = "Cash on delivery" }
                                )
                                Spacer(modifier = Modifier.width(8.dp)) // Space between radio button and text

                                // Icon for Cash on delivery
                                Image(
                                    painter = painterResource(id = R.drawable.ic_cash_on_delivery),
                                    contentDescription = "Cash on delivery icon",
                                    modifier = Modifier.size(24.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text

                                Text(
                                    text = "Cash on delivery",
                                    style = MaterialTheme.typography.body1
                                )
                            }

                            // Row for Credit or Debit Card option
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically // Align vertically in the center
                            ) {
                                RadioButton(
                                    selected = selectedPaymentMethod == "Credit or Debit Card",
                                    onClick = { selectedPaymentMethod = "Credit or Debit Card" }
                                )
                                Spacer(modifier = Modifier.width(8.dp)) // Space between radio button and text

                                // Icon for Credit or Debit Card
                                Image(
                                    painter = painterResource(id = R.drawable.ic_debit_card), // Use your resource icon
                                    contentDescription = "Credit or Debit Card icon",
                                    modifier = Modifier.size(24.dp) // Set icon size
                                )
                                Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text

                                Text(
                                    text = "Credit or Debit Card",
                                    style = MaterialTheme.typography.body1
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))

                    // Place Order Button
                    EShopButton(
                        onClick = {
                            if (selectedPaymentMethod == "Cash on delivery") {
                                showPopup = true
                                viewModel.deleteDraftOrder()

                            } else {
                                navController.navigate(NavigationKeys.PAYMENT_ROUTE)
                            }

                        },
                        text = "Place Order",
                    )
                }
            }
        }
    }
}


