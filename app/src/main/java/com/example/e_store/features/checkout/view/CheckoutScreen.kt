package com.example.e_store.features.checkout.view

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.e_store.R
import com.example.e_store.utils.shared_components.sharedHeader
import com.example.e_store.features.checkout.viewModel.CheckoutViewModel
import com.example.e_store.features.payment.view.PaymentSuccessPopup
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.constants.NavigationKeys
import com.example.e_store.utils.shared_components.EShopButton
import com.example.e_store.utils.shared_components.EShopLoadingIndicator
import com.example.e_store.utils.shared_components.ElevationCard
import com.example.e_store.utils.shared_methods.convertCurrency
import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DraftOrderDetails
import com.example.e_store.utils.shared_models.DraftOrderIDHolder
import com.example.e_store.utils.shared_models.NavigationHolder
import kotlinx.coroutines.delay

@Composable
fun CheckoutScreen(viewModel: CheckoutViewModel, navController: NavHostController) {

    val context = LocalContext.current
    val customerDraftOrderState by viewModel.draftOrder.collectAsStateWithLifecycle()
    var orderDetails by remember { mutableStateOf<DraftOrderDetails?>(null) }
    var couponCode by remember { mutableStateOf(TextFieldValue("")) }
    var selectedPaymentMethod by remember { mutableStateOf("Cash on delivery") }

    val defaultAdd by viewModel.defaultAddress.collectAsStateWithLifecycle()
    var addressDetails by remember { mutableStateOf<Address?>(null) }
    var showToast by remember { mutableStateOf(true) }
    val compositionSuccess by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.paymentsuccessful))
    var showPopup by remember { mutableStateOf(false) }
    var totalAmount by remember { mutableStateOf(0.0) }
    var isDiscountApplied by remember { mutableStateOf(false) }

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

    if (showPopup) {
        PaymentSuccessPopup(
            composition = compositionSuccess,
            message = stringResource(R.string.thank_you)
        )
    }

    // Handle the loading and success/error states for the default address
    when (defaultAdd) {
        is DataState.Loading -> {
            EShopLoadingIndicator()
        }

        is DataState.Success -> {
            val draftOrder = (defaultAdd as DataState.Success<Address?>).data
            draftOrder?.let {
                addressDetails = it
                viewModel.addDeliveryAddress(it)
                Log.d("CheckoutScreen", "Order Details: $it")
            }
        }

        is DataState.Error -> {
            val errorMsg = (defaultAdd as DataState.Error).message
            LaunchedEffect(Unit) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            }

        }
    }

    // Handle the loading and success/error states for the draft order
    when (customerDraftOrderState) {
        is DataState.Loading -> {
            EShopLoadingIndicator()
        }

        is DataState.Success -> {
            val draftOrder = (customerDraftOrderState as DataState.Success<DraftOrderDetails?>).data
            draftOrder?.let {
                orderDetails = it
                totalAmount = it.total_price?.toDouble() ?: 0.0 // Calculate total amount here

                Log.d("CheckoutScreen", "Order Details: $it")

                if (totalAmount >= 1000) {
                    selectedPaymentMethod = stringResource(R.string.credit_or_debit_card)
                    if (showToast) {
                        Toast.makeText(
                            context,
                            stringResource(R.string.total_exceeds_cash_limit_credit_selected),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    showToast = false
                }
            }
        }

        is DataState.Error -> {
            val errorMsg = (customerDraftOrderState as DataState.Error).message
            LaunchedEffect(Unit) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Only render the UI when order details are available
    orderDetails?.let { details ->
        if(details.applied_discount != null)
        {
            isDiscountApplied = true
        }
        // Apply contentPadding to the Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            sharedHeader(
                navController = navController,
                headerText = stringResource(id = R.string.checkout)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .padding(16.dp, 30.dp, 16.dp, 16.dp), // Additional padding for the content
            ) {
                // Address Section
                ElevationCard {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(Color.White)
                    ) {
                        if (addressDetails != null) {
                            Text(
                                text = stringResource(R.string.choose_your_delivery_address),
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = addressDetails?.address1 ?: "",
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
                                TextButton(onClick = {
                                    NavigationHolder.id = addressDetails?.id
                                    navController.navigate(NavigationKeys.ADD_LOCATION_ROUTE)
                                }) {
                                    Text(
                                        text = "Update Current Address",
                                        fontSize = 18.sp,
                                        color = PrimaryColor
                                    )
                                }
                                TextButton(onClick = {
                                    navController.navigate(NavigationKeys.LOCATION_ROUTE)
                                }) {
                                    Text(
                                        text = stringResource(R.string.choose_another_address),
                                        fontSize = 18.sp,
                                        color = PrimaryColor
                                    )
                                }
                            }
                        } else {
                            // No default address, prompt user to choose one
                            Text(
                                text = stringResource(R.string.no_address_found),
                                fontSize = 18.sp,
                                color = Color.Red,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            TextButton(onClick = {
                                navController.navigate(NavigationKeys.LOCATION_ROUTE)
                            }) {
                                Text(
                                    text = stringResource(R.string.choose_your_delivery_address),
                                    fontSize = 18.sp,
                                    color = PrimaryColor
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                CouponCodeSection(viewModel = viewModel, isDiscountApplied = isDiscountApplied)

                Spacer(modifier = Modifier.height(30.dp))

                // Order Summary
                ElevationCard(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.order_summary),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = stringResource(R.string.items), fontSize = 16.sp)
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
                            Text(text = stringResource(R.string.subtotal), fontSize = 16.sp)
                            Text(
                                text = "${details.subtotal_price?.let { convertCurrency(it.toDouble()) }}",
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = stringResource(R.string.discount), fontSize = 16.sp)
                            Text(
                                text = convertCurrency(
                                    details.applied_discount?.value?.toDouble() ?: 0.0
                                ),
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = stringResource(R.string.total_tax), fontSize = 16.sp)
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
                                text = stringResource(R.string.total),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                details.total_price?.let { convertCurrency(it.toDouble()) }
                                    ?: "0.0",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
                ElevationCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp) // Padding inside the card
                    ) {
                        // Payment Method Section
                        Text(
                            text = stringResource(R.string.choose_payment_method),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp) // Spacing below the title
                        )

                        // Conditional rendering of payment options based on total amount
                        if (totalAmount < 1000) {
                            PaymentOptionRow(
                                isSelected = selectedPaymentMethod == context.getString(R.string.cash_on_delivery),
                                onSelect = {
                                    selectedPaymentMethod =
                                        context.getString(R.string.cash_on_delivery)
                                },
                                label = stringResource(id = R.string.cash_on_delivery)
                            )
                        }

                        PaymentOptionRow(
                            isSelected = selectedPaymentMethod == context.getString(R.string.credit_or_debit_card),
                            onSelect = {
                                selectedPaymentMethod =
                                    context.getString(R.string.credit_or_debit_card)
                            },
                            label = stringResource(id = R.string.credit_or_debit_card)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        EShopButton(
                            text = stringResource(R.string.place_order),
                            onClick = {
                                if (addressDetails == null) {
                                    Log.d("CheckoutScreen", "No address selected")
                                    Toast.makeText(context, "Choose an address first", Toast.LENGTH_SHORT).show()
                                }else{
                                // Trigger payment processing logic
                                processPayment(
                                    selectedPaymentMethod,
                                    navController,
                                    context,
                                    viewModel,
                                    { showPopup = true },
                                )
                                }
                            },
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun PaymentOptionRow(isSelected: Boolean, onSelect: () -> Unit, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconResId = if (label == stringResource(R.string.credit_or_debit_card)) {
            R.drawable.ic_debit_card
        } else {
            R.drawable.ic_cash_on_delivery
        }


        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(selectedColor = PrimaryColor),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = if (label == stringResource(R.string.credit_or_debit_card)) {
                stringResource(R.string.credit_or_debit_card_icon)
            } else {
                stringResource(R.string.cash_on_delivery_icon)
            },
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, fontSize = 16.sp)
    }
}


private fun processPayment(
    selectedPaymentMethod: String,
    navController: NavController,
    context: Context,
    viewModel: CheckoutViewModel,
    setShowPopup: () -> Unit
) {
    if (selectedPaymentMethod == context.getString(R.string.cash_on_delivery)) {
        viewModel.sendEmailAndDeleteDraftOrder()
        setShowPopup()
    } else{
        navController.navigate(NavigationKeys.PAYMENT_ROUTE)

    }
}
@Composable
fun CouponCodeSection(viewModel: CheckoutViewModel, isDiscountApplied: Boolean ) {
    val context = LocalContext.current
    var couponCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var isCouponApplied by remember { mutableStateOf(isDiscountApplied) }
    // Track if coupon is applied

    Column {
        TextField(
            value = couponCode,
            onValueChange = {
                couponCode = it
                // Clear error and success messages on input change
                if (errorMessage.isNotEmpty()) {
                    errorMessage = ""
                }
                successMessage = "" // Clear success message on new input
            },
            placeholder = { Text(stringResource(R.string.enter_coupon_code)) },
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
                        // Validate the coupon code here
                        if (couponCode.isEmpty()) {
                            errorMessage = context.getString(R.string.coupon_code_cannot_be_empty)
                            successMessage = "" // Clear success message
                        } else {
                            errorMessage = "" // Clear previous error
                            isLoading = true // Start loading

                            // Call applyDiscount with the coupon code and a callback
                            viewModel.applyDiscount(couponCode) { success ->
                                isLoading = false // Stop loading
                                if (success) {
                                    errorMessage = ""
                                    successMessage = context.getString(R.string.coupon_code_applied_successfully) // Success message
                                    isCouponApplied = true
                                } else {
                                    successMessage = ""
                                    errorMessage = context.getString(R.string.failed_to_apply_coupon_code) // Set error message
                                }
                            }
                        }
                    },
                    enabled = !isCouponApplied
                ) {
                    Text(
                        text = if (isCouponApplied || isDiscountApplied) stringResource(R.string.applied)
                        else if (isLoading) stringResource(R.string.applying)
                        else stringResource(R.string.apply)
                    )
                }
            }
        )

        // Show loading indicator if isLoading is true
        if (isLoading) {
            EShopLoadingIndicator() // Your loading indicator
        }

        // Display success message when the coupon is applied
        if (isCouponApplied && successMessage.isNotEmpty()||isDiscountApplied) {
            Text(
                text = successMessage,
                color = Color.Green, // Customize the color as needed
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Display error message if there is one
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red, // Customize color for error messages
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
