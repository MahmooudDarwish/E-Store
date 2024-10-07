package com.example.e_store.features.product_info.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.e_store.features.product_info.view_model.ProductInfoViewModel
import com.example.e_store.ui.theme.LightGreen
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.shared_components.BackButton
import com.example.e_store.utils.shared_models.LineItem
import com.example.e_store.utils.shared_models.ProductDetails
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.example.e_store.utils.shared_models.Property

@Composable
fun ProductInfoScreen(navController: NavHostController, viewModel: ProductInfoViewModel) {

    //val viewModel: ProductInfoViewModel = viewModel()
    val selectedSize by viewModel.selectedSize
    val selectedColor by viewModel.selectedColor
    val price by viewModel.price
    val stock by viewModel.stock
    val allReviewsVisible by viewModel.allReviewsVisible
    val favoriteIcon by viewModel.favoriteIcon
    val context = LocalContext.current

    var showQuantityDialog by remember { mutableStateOf(false) }
    var quantityInput by remember { mutableStateOf(TextFieldValue("1")) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                ImageSliderWithPager(ProductDetails.images)

                BackButton(onBackClick = { navController.popBackStack() })
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-20).dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(10),
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = ProductDetails.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = ProductDetails.vendor,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = price,
                        style = MaterialTheme.typography.titleLarge,
                        color = PrimaryColor
                    )
                    Text(
                        text = "$stock in stock",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (stock > 0) LightGreen else Color.Red
                    )
                    Text(
                        text = ProductDetails.description,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text("Size", style = MaterialTheme.typography.titleSmall)
                        CustomChip(
                            options = ProductDetails.sizes,
                            selectedOption = selectedSize,
                            onSelect = { newSize -> viewModel.updateSelectedSize(newSize) }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Color", style = MaterialTheme.typography.titleSmall)
                        CustomChip(
                            options = ProductDetails.colors,
                            selectedOption = selectedColor,
                            onSelect = { newColor -> viewModel.updateSelectedColor(newColor) }
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(10),
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Reviews",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = { viewModel.toggleAllReviews() }) {
                            Text(if (allReviewsVisible) "Show less" else "See more reviews")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (allReviewsVisible) {
                        reviews.forEach { review ->
                            ReviewItem(review = review)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    } else {
                        reviews.take(2).forEach { review ->
                            ReviewItem(review = review)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(80.dp),
            elevation = 8.dp,
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.toggleFavorite() }) {
                    Icon(
                        imageVector = if (favoriteIcon) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                        contentDescription = null,
                        tint = if (favoriteIcon) PrimaryColor else Color.Gray
                    )
                }
                if (showQuantityDialog) {
                    AlertDialog(
                        onDismissRequest = { showQuantityDialog = false },
                        title = { Text(text = "Enter Quantity") },
                        text = {
                            Column {
                                Text("Available stock: $stock")
                                androidx.compose.material.TextField(
                                    value = quantityInput,
                                    onValueChange = { newValue ->
                                        // Ensure the input is numeric and within stock limits
                                        if (newValue.text.all { it.isDigit() }) {
                                            val quantity = newValue.text.toIntOrNull() ?: 0
                                            if (quantity <= stock) {
                                                quantityInput = newValue
                                            }
                                        }
                                    },
                                    label = { Text("Quantity") },
                                    singleLine = true
                                )
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                // Check if the quantity is valid before confirming
                                val quantity = quantityInput.text.toIntOrNull() ?: 0
                                if (quantity in 1..3) {
                                    showQuantityDialog = false

                                    viewModel.createDraftOrder(
                                        lineItemList = createDraftOrderItems(

                                            quantity = quantity,
                                            selectedSize = selectedSize,
                                            selectedColor = selectedColor,
                                            context = context,
                                        ),
                                        isFavorite = false,
                                        productID = ProductDetails.id

                                    )

                                    Toast.makeText(
                                        context,
                                        "Items Added to Cart Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }) {
                                Text("Confirm")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showQuantityDialog = false }) {
                                Text("Cancel")


                            }
                        }
                    )
                }

                Button(
                    onClick = {
                        showQuantityDialog = true

                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Add to Cart")
                    }
                }
            }
        }
    }


}

fun createDraftOrderItems(
    selectedSize: String,
    selectedColor: String,
    quantity: Int,
    context: Context,
): MutableList<LineItem> {
    val lineItemsList = mutableListOf<LineItem>()

    // Find the matching variant based on selected size and color
    val variant =
        ProductDetails.variants.find { it.option1 == selectedSize && it.option2 == selectedColor }

    // Extract price and variantId from the found variant
    val price = variant?.price
    val variantId = variant?.id

    if (price != null && variantId != null) {
        val properties: List<Property> = listOf(
            Property("Size", selectedSize),    // Capitalized "Size"
            Property("Color", selectedColor),  // Capitalized "Color"
        )

        // Create a line item and add it to the list
        val lineItem = LineItem(
            title = ProductDetails.title,
            price = price,
            quantity = quantity,
            variant_id = variantId.toString(),
            properties = properties
        )

        lineItemsList.add(lineItem)
    } else {
        // Handle the case where the variant is not found
        Toast.makeText(context, "Selected variant not available", Toast.LENGTH_SHORT).show()
    }

    return lineItemsList
}


@Composable
fun ReviewItem(review: Review) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = review.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Row {
                repeat(review.rating) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = review.comment,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}


private fun updatePriceAndStock(
    product: ProductDetails,
    selectedSize: String,
    selectedColor: String,
    price: MutableState<String>,
    stock: MutableState<Int>
) {
    val variant =
        product.variants.find { it.option1 == selectedSize && it.option2 == selectedColor }
    if (variant != null) {
        price.value = variant.price
        stock.value = variant.inventoryQuantity
    }
}


@Composable
fun CustomChip(
    options: List<String>,
    selectedOption: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(modifier = modifier) {
        items(options) { option ->
            FilterChip(
                selected = option == selectedOption,
                onClick = { onSelect(option) },
                label = {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                },
                colors = FilterChipDefaults.filterChipColors(),
                modifier = Modifier.padding(horizontal = 4.dp) // Add padding to separate chips
            )
        }
    }
}


@Composable
fun ImageSliderWithPager(images: List<String>) {
    val pagerState = rememberPagerState()

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            count = images.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
        ) { page ->
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(images[page])
                        .size(300, 300)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
            activeColor = PrimaryColor,
            inactiveColor = Color.Gray
        )
    }
}


data class Review(
    val name: String,
    val comment: String,
    val rating: Int
)

val reviews = listOf(
    Review("Kareem", "Quality is not as advertised.", 1),
    Review("Dina", "Very happy with my purchase.", 5),
    Review("Ali", "Not bad.", 3),
    Review("Layla", "Absolutely love it! Great buy.", 5),
    Review("Tamer", "Product arrived damaged.", 2),
    Review("Fatma", "Surprisingly good quality.", 4),
    Review("Adel", "Not worth the price.", 2),
    Review("Salma", "Exceeded my expectations!", 5),
    Review("Ibrahim", "Wouldn't recommend.", 1),
    Review("Reem", "Good, but shipping took too long.", 3)
)



