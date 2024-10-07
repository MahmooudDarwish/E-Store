package com.example.e_store.features.shopping_cart.view

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.e_store.R
import com.example.e_store.features.shopping_cart.view_model.ShoppingCartViewModel
import com.example.e_store.utils.shared_components.ElevationCard
import com.example.e_store.utils.shared_components.LottieWithText
import com.example.e_store.utils.shared_models.LineItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ShoppingCartScreen ( viewModel: ShoppingCartViewModel) {
    //viewModel.fetchShoppingCartItems()

    LaunchedEffect(Unit) {
        viewModel.fetchShoppingCartItems()
    }

    viewModel.fetchCustomerShoppingCartDraftOrders()
    val draftOrderItems by viewModel.shoppingCartItems.collectAsState()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.addtocart))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever // Loop the animation infinitely
    )

    var showAllItems by remember { mutableStateOf(false) }


    if (draftOrderItems.isEmpty()) {
        LottieWithText(
            displayText = stringResource(id = R.string.no_products_shopping_message),
            lottieRawRes = R.raw.no_data_found
        )
    } else {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .background(Color.White),
        ) {
            val displayedItems = if (showAllItems) draftOrderItems else draftOrderItems.take(2)
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(displayedItems) { item ->
                    SwipeableItem(viewModel,item) { deletedItem ->
                        deletedItem.product_id?.let { viewModel.removeShoppingCartDraftOrder(it, deletedItem.variant_id.toLong()) }
                        // Here, you would update your ViewModel to handle the deletion of an item
                        //viewModel.removeItemFromDraftOrder(deletedItem)
                    }
                }
                if (draftOrderItems.size > 2) {  // Display "See More" when there are more than 2 items
                    item {
                        Text(
                            text = if (showAllItems) stringResource(R.string.see_less) else stringResource(R.string.see_more),
                            modifier = Modifier
                                .clickable {
                                    showAllItems = !showAllItems
                                    Log.d(
                                        " Shopping Cart",
                                        "Current state of showAllItems: $showAllItems"
                                    ) // Debug log
                                }
                                .padding(vertical = 8.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.total),
                style = MaterialTheme.typography.titleLarge,
            )
            Button(
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.colorPrimaryButton)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.checkout),
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun SwipeableItem(viewModel: ShoppingCartViewModel,item: LineItem, onItemDeleted: (LineItem) -> Unit) {
  val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.ic_delete))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever // Loop the animation infinitely
    )
    // Use Animatable for smooth swipe animations
    val offsetX = remember { Animatable(0f) }
    val deleteIconVisible by remember { derivedStateOf { offsetX.value < -100 } }
    val coroutineScope = rememberCoroutineScope() // Create a coroutine scope
    var shouldCheckStock by remember { mutableStateOf(false) }

    if (shouldCheckStock) {
        LaunchedEffect(Unit) {
            delay(3000) // Add 3-second delay before checking stock

            val productCountInCart = item.quantity
            val availableStock = viewModel.shoppingCartItemsCount.value

            if (availableStock != null) {
                if (productCountInCart < 3 && availableStock > productCountInCart) {
                    item.product_id?.let {
                        viewModel.increaseProductQuantity(it, item.variant_id.toLong())
                    }
                } else if (productCountInCart >= 3) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.maximum_quantity_reached),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (availableStock <= productCountInCart) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.insufficient_stock),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            shouldCheckStock = false // Reset the flag after the stock check
        }
    }
    if (showDialog) {
        AnimatedVisibility(
            visible = showDialog,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            AlertDialog(
                containerColor = Color.White,

                onDismissRequest = { showDialog = false },
                title = { Text(stringResource(R.string.confirm_delete)) },
                text = { Text(stringResource(R.string.are_you_sure_you_want_to_delete, item)) },
                confirmButton = {
                    TextButton(onClick = {
                        coroutineScope.launch { offsetX.snapTo(0f) } // Reset swipe
                        onItemDeleted(item)
                        showDialog = false
                    }) {
                        Text(stringResource(R.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(stringResource(R.string.dismiss))
                    }
                }

            )
        }
    }

    ElevationCard (
    ){

        AnimatedVisibility(
            visible = deleteIconVisible,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        showDialog = true
                    }
                    .background(
                        Color.Red.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(50)
                    ) // Background for better visibility
                    .clip(RoundedCornerShape(50)) // Circular background
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        val newOffset = (offsetX.value + dragAmount).coerceIn(-300f, 0f)
                        change.consume()

                        coroutineScope.launch {
                            offsetX.snapTo(newOffset)
                        }
                    }
                }
                .clip(RoundedCornerShape(16.dp))
        ) {


            Row(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model =item.properties.get(2).value,
                    contentDescription = stringResource(R.string.product_image),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = colorResource(id = R.color.colorTextCardHeader)
                    )

                    Text(
                        text = stringResource(R.string.price )+ ((item.price.toDouble())*item.quantity).toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorResource(id = R.color.colorTextCardBody)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            Log.d("ShoppingCart", "Product ID: ${item.product_id}")

                            // Fetch the product ID and current stock information
                            item.product_id?.let { viewModel.fetchProductByID(it, item.variant_id.toLong()) }

                            // Set a flag to trigger the stock check in a composable scope
                            shouldCheckStock = true
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_add_square),
                                contentDescription = stringResource(R.string.increase_quantity),
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = item.quantity.toString(), fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = {
                            item.product_id?.let { viewModel.decreaseProductQuantity(it, item.variant_id.toLong()) }
                            if (item.quantity == 0) {
                                onItemDeleted(item)
                            }

                        }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_minus_square),
                                contentDescription = stringResource(R.string.decrease_quantity),
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }


            }
        }
    }
}



