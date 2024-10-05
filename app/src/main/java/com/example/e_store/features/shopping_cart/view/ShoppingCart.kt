package com.example.e_store.features.shopping_cart.view

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
@Composable
fun ShoppingCartScreen() {
    val cartItems = remember { mutableStateListOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5") }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.addtocart))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever // Loop the animation infinitely
    )

    var showAllItems by remember { mutableStateOf(false) }

    if (cartItems.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            )
        }
    } else {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize()
        ) {
            val displayedItems = if (showAllItems) cartItems else cartItems.take(2)

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp) // Space between items
            ) {
                items(displayedItems) { item ->
                    SwipeableItem(item) { deletedItem ->
                        cartItems.remove(deletedItem)
                    }
                }

                if (cartItems.size > 3) {
                    item {
                        Text(
                            text = if (showAllItems) "See Less" else "See More",
                            modifier = Modifier
                                .clickable { showAllItems = !showAllItems }
                                .padding(vertical = 8.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Text(
                text = "Total: $100.00",
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
                    text = "Checkout",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun SwipeableItem(item: String, onItemDeleted: (String) -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.ic_delete))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever // Loop the animation infinitely
    )
    // Use Animatable for smooth swipe animations
    val offsetX = remember { Animatable(0f) }
    val deleteIconVisible by remember { derivedStateOf { offsetX.value < -100 } }
    val coroutineScope = rememberCoroutineScope() // Create a coroutine scope

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .padding(8.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    val newOffset = (offsetX.value + dragAmount).coerceIn(-300f, 0f)
                    change.consume()

                    coroutineScope.launch {
                    // Animate swipe effect with a smooth transition
                    offsetX.snapTo(newOffset)
                        }
                }
            }
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "https://img-cdn.pixlr.com/image-generator/history/65bb506dcb310754719cf81f/ede935de-1138-4f66-8ed7-44bd16efc709/medium.webp",
                    contentDescription = "Product image",
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
                        text = item,
                        style = MaterialTheme.typography.titleLarge,
                        color = colorResource(id = R.color.colorTextCardHeader)
                    )

                    Text(
                        text = "$10.00",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorResource(id = R.color.colorTextCardBody)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Increase and Decrease Quantity Buttons
                        IconButton(onClick = { /* Handle click */ }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_add_square),
                                contentDescription = "Increase quantity",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "1", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = { /* Handle click */ }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_minus_square),
                                contentDescription = "Decrease quantity",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }

                // Display delete icon with fade animation
                AnimatedVisibility(
                    visible = deleteIconVisible,
                    enter = fadeIn() + scaleIn(), // Add scale effect when icon appears
                    exit = fadeOut() + scaleOut()
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = progress,
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.CenterVertically)
                                .padding(start = 16.dp)
                            .clickable {
                                coroutineScope.launch {
                                    offsetX.snapTo(0f) // Reset swipe when delete is clicked
                                }
                                onItemDeleted(item)
                            }
                    )

                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCardItemRow() {
    ShoppingCartScreen()
}
