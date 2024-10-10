package com.example.e_store.features.product_info.view

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.e_store.R
import com.example.e_store.features.product_info.view_model.ProductInfoViewModel
import com.example.e_store.ui.theme.ButtonColor
import com.example.e_store.ui.theme.LightGreen
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.shared_components.BackButton
import com.example.e_store.utils.shared_components.EShopButton
import com.example.e_store.utils.shared_components.EShopLoadingIndicator
import com.example.e_store.utils.shared_methods.convertCurrency
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.LineItem
import com.example.e_store.utils.shared_models.ProductDetails
import com.example.e_store.utils.shared_models.Property
import com.example.e_store.utils.shared_models.UserSession
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProductInfoScreen(navController: NavHostController, viewModel: ProductInfoViewModel) {


    val productState by viewModel.productState.collectAsStateWithLifecycle()

    //Log.d("ProductInfoScreen", "ProductDetails: $productState")


    if (ProductDetails.isNavigationFromFavourites) {
        LaunchedEffect(Unit) {
            viewModel.fetchProductById()
        }

        when (productState) {
            is DataState.Loading -> {
                EShopLoadingIndicator()
                Log.d("ProductInfoScreen", "Loading")
            }

            is DataState.Success -> {
                val product = (productState as DataState.Success).data.product
                Log.d("ProductInfoScreen", "Success")
                Log.d("ProductInfoScreen", "Product: $product")
                ProductDetails(navController, viewModel)
            }

            is DataState.Error -> {
                Toast.makeText(
                    LocalContext.current,
                    (productState as DataState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        ProductDetails.isNavigationFromFavourites = false
    } else {
        ProductDetails(navController, viewModel)
    }


}

@Composable
fun ProductDetails(navController: NavHostController, viewModel: ProductInfoViewModel) {
    var selectedQuantity by remember { mutableIntStateOf(1) }

    val selectedSize by viewModel.selectedSize
    val selectedColor by viewModel.selectedColor
    val price by viewModel.price
    val stock by viewModel.stock
    val allReviewsVisible by viewModel.allReviewsVisible
    val favoriteIcon by viewModel.favoriteIcon
    val context = LocalContext.current
    var isConfirmState by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(0f) }



    LaunchedEffect(Unit) {
        viewModel.fetchAllDraftOrders(true)
        if (isConfirmState) {
            rotation.animateTo(
                360f, animationSpec = tween(durationMillis = 600)
            )

        } else {
            rotation.animateTo(0f, animationSpec = tween(durationMillis = 600))


        }
    }

    var showQuantitySection by remember { mutableStateOf(false) }

    // Animation state for rotation


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
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    // Log.d("ProductInfoScreen", "ProductDetails: ${ProductDetails.title}")
                    Text(
                        text = ProductDetails.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = ProductDetails.vendor,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                        text = ProductDetails.price,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "$stock in stock",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (stock > 0) Color(0xFF4CAF50) else Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = ProductDetails.description,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Size", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    CustomChip(
                        options = ProductDetails.sizes,
                        selectedOption = selectedSize,
                        onSelect = { newSize -> viewModel.updateSelectedSize(newSize) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Color", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    CustomChip(
                        options = ProductDetails.colors,
                        selectedOption = selectedColor,
                        onSelect = { newColor -> viewModel.updateSelectedColor(newColor) }
                    )
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
        if (!UserSession.isGuest) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                elevation = 8.dp,
                color = Color.White
            ) {
                Column {

                    AnimatedVisibility(
                        visible = showQuantitySection,
                        enter = slideInVertically(initialOffsetY = { it }),
                        exit = slideOutVertically(targetOffsetY = { -it })
                    ) {
                        val validQuantities = (1..stock.coerceAtMost(3)).toList()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Choose Quantity:")

                            validQuantities.forEach { quantity ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = (selectedQuantity == quantity),
                                        onClick = { selectedQuantity = quantity }
                                    )
                                    Text(text = "$quantity")
                                }
                            }

                            Icon(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .clickable {
                                        isConfirmState = false
                                        showQuantitySection = false
                                    },
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close"
                            )
                        }
                    }


                    // Row for Favorite Icon and Buttons
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            if (viewModel.favoriteIcon.value) {
                                viewModel.toggleFavorite()
                                viewModel.removeShoppingCartDraftOrder(
                                    productId = ProductDetails.id,
                                    variantId = ProductDetails.variants.first().id
                                )
                            } else {
                                viewModel.toggleFavorite()
                                viewModel.createDraftOrder(
                                    lineItemList = createDraftOrderItems(
                                        quantity = 1,
                                        selectedSize = selectedSize,
                                        selectedColor = selectedColor,
                                        context = context,
                                    ), isFavorite = true, productID = ProductDetails.id
                                )
                            }
                        }) {
                            Icon(
                                imageVector = if (favoriteIcon) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                                contentDescription = null,
                                tint = if (favoriteIcon) PrimaryColor else Color.Gray
                            )
                        }

                        // Add to Cart and Confirm Buttons
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Add to Cart Button
                            AnimatedVisibility(visible = !isConfirmState) {
                                EShopButton(
                                    text = "Add to Cart",
                                    onClick = {
                                        if (stock == 0) {
                                            Toast.makeText(
                                                context,
                                                "Out of Stock",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            isConfirmState = true
                                            showQuantitySection = true
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .fillMaxWidth()
                                        .graphicsLayer(
                                            rotationY = rotation.value,
                                            cameraDistance = 12f
                                        ),
                                )

                            }

                            // Confirm Button
                            AnimatedVisibility(visible = isConfirmState) {
                                EShopButton(
                                    text = "Confirm",
                                    onClick = {
                                        viewModel.createDraftOrder(
                                            lineItemList = createDraftOrderItems(
                                                quantity = selectedQuantity,
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
                                        showQuantitySection = false
                                        isConfirmState = false
                                    },
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .fillMaxWidth()
                                        .graphicsLayer(
                                            rotationY = rotation.value,
                                            cameraDistance = 12f
                                        ),
                                )
                            }
                        }
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
            Property("imageUrl", ProductDetails.images[0])
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
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
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
                        tint = PrimaryColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = review.comment, style = MaterialTheme.typography.bodyMedium, color = Color.Gray
        )
    }
}


private fun updatePriceAndStock(
    product: ProductDetails,
    selectedSize: String,
    selectedColor: String,
    price: MutableState<String>,
    stock: MutableState<Int>,
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
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(options) { option ->
            val isSelected = option == selectedOption
            Surface(
                shape = RoundedCornerShape(50),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                ),
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface,
                modifier = Modifier.clickable { onSelect(option) }
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}


@Composable
fun ImageSliderWithPager(images: List<String>) {
    val pagerState = rememberPagerState()

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            count = images.size, state = pagerState, modifier = Modifier.fillMaxWidth()
        ) { page ->
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current).data(images[page])
                        .size(300, 300).crossfade(true).build()
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
    val name: String, val comment: String, val rating: Int,
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



